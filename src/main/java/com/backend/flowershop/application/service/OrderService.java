package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.request.CreateOrderRequestDTO;
import com.backend.flowershop.application.dto.response.CartItemDTOResponse;
import com.backend.flowershop.domain.model.Order;
import com.backend.flowershop.domain.model.OrderItem;
import com.backend.flowershop.domain.repository.CartRepository;
import com.backend.flowershop.domain.repository.FlowerRepository;
import com.backend.flowershop.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final FlowerRepository flowerRepository;

    public OrderService(OrderRepository orderRepository,
                        CartService cartService,
                        CartRepository cartRepository,
                        FlowerRepository flowerRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.cartRepository = cartRepository;
        this.flowerRepository = flowerRepository;
    }

    /**
     * 下单核心逻辑 (原子性事务)
     */
    @Transactional
    public Long checkout(String userId, CreateOrderRequestDTO request) {
        // 1. 获取购物车
        List<CartItemDTOResponse> cartItems = cartService.getMyCart(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 2. 校验库存 & 计算总价
        BigDecimal total = BigDecimal.ZERO;
        for (CartItemDTOResponse item : cartItems) {
            // 注意：CartItemDTOResponse 是 Record，使用 item.flowerId() 而非 getFlowerId()
            int rowsAffected = flowerRepository.reduceStock(item.flowerId(), item.quantity());
            if (rowsAffected == 0) {
                throw new RuntimeException("Stock insufficient for flower: " + item.name());
            }

            // 类型转换：Double -> BigDecimal
            BigDecimal itemSubtotal = BigDecimal.valueOf(item.subtotal());
            total = total.add(itemSubtotal);
        }

        // 3. 保存订单主表
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(total);
        order.setStatus("PAID"); // 简化：默认已支付
        order.setShippingAddress(request.getShippingAddress());

        Long orderId = orderRepository.saveOrder(order);

        // 4. 保存订单详情 (快照)
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItemDTOResponse item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setFlowerId(item.flowerId());
            orderItem.setFlowerName(item.name());
            orderItem.setPriceAtPurchase(BigDecimal.valueOf(item.price())); // 记录购买时的价格
            orderItem.setQuantity(item.quantity());
            orderItems.add(orderItem);
        }
        orderRepository.saveOrderItems(orderItems);

        // 5. 清空购物车
        cartRepository.deleteAllByUserId(userId);

        return orderId;
    }

    /**
     * 获取用户历史订单 (已解决 N+1 问题)
     * 策略：两次查询 + 内存组装
     */
    public List<Order> getUserOrders(String userId) {
        // 1. 第一次查询：获取所有订单主表数据
        List<Order> orders = orderRepository.findByUserId(userId);

        if (orders.isEmpty()) {
            return Collections.emptyList();
        }

        // 2. 第二次查询：一次性获取该用户所有订单下的商品详情
        // (这需要 Repository 提供一个新方法：findOrderItemsByUserId)
        List<OrderItem> allItems = orderRepository.findOrderItemsByUserId(userId);

        // 3. 内存分组：按 OrderID 将商品归类
        Map<Long, List<OrderItem>> itemsByOrderId = allItems.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderId));

        // 4. 组装：将商品列表塞回对应的订单对象中
        for (Order order : orders) {
            List<OrderItem> items = itemsByOrderId.getOrDefault(order.getId(), new ArrayList<>());
            order.setItems(items);
        }

        return orders;
    }
}
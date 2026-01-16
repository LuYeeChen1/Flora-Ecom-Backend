package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.request.CreateOrderRequestDTO;
import com.backend.flowershop.application.dto.response.CartItemDTOResponse;
import com.backend.flowershop.domain.enums.OrderStatus; // ✅ 引入
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

    @Transactional
    public Long checkout(String userId, String userEmail, CreateOrderRequestDTO request) {
        List<CartItemDTOResponse> cartItems = cartService.getMyCart(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        BigDecimal total = BigDecimal.ZERO;
        for (CartItemDTOResponse item : cartItems) {
            int rowsAffected = flowerRepository.reduceStock(item.flowerId(), item.quantity());
            if (rowsAffected == 0) {
                throw new RuntimeException("Stock insufficient for flower: " + item.name());
            }
            BigDecimal itemSubtotal = BigDecimal.valueOf(item.subtotal());
            total = total.add(itemSubtotal);
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(total);

        // ✅ [核心变更] 初始状态设为 Enum
        order.setStatus(OrderStatus.PAID);

        order.setReceiverEmail(userEmail);
        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setShippingAddress(request.getShippingAddress());

        Long orderId = orderRepository.saveOrder(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItemDTOResponse item : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setFlowerId(item.flowerId());
            orderItem.setFlowerName(item.name());
            orderItem.setPriceAtPurchase(BigDecimal.valueOf(item.price()));
            orderItem.setQuantity(item.quantity());
            orderItems.add(orderItem);
        }
        orderRepository.saveOrderItems(orderItems);
        cartRepository.deleteAllByUserId(userId);

        return orderId;
    }

    public List<Order> getUserOrders(String userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        if (orders.isEmpty()) return Collections.emptyList();

        List<OrderItem> allItems = orderRepository.findOrderItemsByUserId(userId);
        Map<Long, List<OrderItem>> itemsByOrderId = allItems.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderId));

        for (Order order : orders) {
            order.setItems(itemsByOrderId.getOrDefault(order.getId(), new ArrayList<>()));
        }
        return orders;
    }

    // --- 状态流转逻辑 ---

    @Transactional
    // ✅ [核心变更] 参数接收 OrderStatus Enum
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        // 校验状态流转 (Enum 比较)
        validateStateTransition(order.getStatus(), newStatus);

        // 更新状态
        orderRepository.updateStatus(orderId, newStatus);
    }

    private void validateStateTransition(OrderStatus current, OrderStatus next) {
        // 允许: PAID -> SHIPPED
        if (current == OrderStatus.PAID && next == OrderStatus.SHIPPED) return;

        // 允许: SHIPPED -> DELIVERED
        if (current == OrderStatus.SHIPPED && next == OrderStatus.DELIVERED) return;

        // 幂等性 (重复点没关系)
        if (current == next) return;

        throw new RuntimeException("Invalid status transition from " + current + " to " + next);
    }

    @Transactional
    public void requestCancel(Long orderId, String userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 安全检查: 只能取消自己的订单
        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to order");
        }

        // 状态机检查: 只有 PAID 状态可以申请取消
        if (order.getStatus() != OrderStatus.PAID) {
            throw new RuntimeException("Cannot cancel order in state: " + order.getStatus());
        }

        // 更新状态为 "申请中"
        orderRepository.updateStatus(orderId, OrderStatus.CANCELLATION_REQUESTED);

        // 可选: 发送通知给卖家 (TODO: NotificationService)
    }
}
package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.request.CreateOrderRequestDTO;
import com.backend.flowershop.application.dto.response.CartItemDTOResponse;
import com.backend.flowershop.domain.model.Order;
import com.backend.flowershop.domain.model.OrderItem;
import com.backend.flowershop.domain.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final FlowerRepository flowerRepository;

    public OrderService(OrderRepository orderRepository, CartService cartService,
                        CartRepository cartRepository, FlowerRepository flowerRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.cartRepository = cartRepository;
        this.flowerRepository = flowerRepository;
    }

    @Transactional
    public Long checkout(String userId, CreateOrderRequestDTO request) {
        List<CartItemDTOResponse> cartItems = cartService.getMyCart(userId);
        if (cartItems.isEmpty()) throw new RuntimeException("Cart is empty");

        BigDecimal total = BigDecimal.ZERO;
        for (CartItemDTOResponse item : cartItems) {
            int rowsAffected = flowerRepository.reduceStock(item.flowerId(), item.quantity());
            if (rowsAffected == 0) throw new RuntimeException("Stock insufficient for flower: " + item.name());
            total = total.add(BigDecimal.valueOf(item.subtotal()));
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(total);
        order.setStatus("PAID");
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

        orders.forEach(o -> o.setItems(itemsByOrderId.getOrDefault(o.getId(), new ArrayList<>())));
        return orders;
    }
}
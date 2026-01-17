package com.backend.flowershop.domain.repository;

import com.backend.flowershop.application.dto.response.SellerOrderDTOResponse;
import com.backend.flowershop.domain.enums.OrderStatus;
import com.backend.flowershop.domain.model.Order;
import com.backend.flowershop.domain.model.OrderItem;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    // Buyer Methods
    Long saveOrder(Order order);
    void saveOrderItems(List<OrderItem> items);
    List<Order> findByUserId(String userId);
    Optional<Order> findById(Long id);
    Order findById(Long orderId, String userId);
    List<OrderItem> findOrderItemsByUserId(String userId);

    // Seller Methods
    List<SellerOrderDTOResponse> findOrdersBySellerId(String sellerId);

    // ✅ [新增] 查询单个订单详情 (用于卖家详情页)
    Optional<SellerOrderDTOResponse> findOrderByIdAndSellerId(Long orderId, String sellerId);

    // 状态更新
    void updateStatus(Long orderId, OrderStatus status);
    void updateItemsStatusBySeller(Long orderId, String sellerId, OrderStatus status);
    boolean isOrderFullyShipped(Long orderId);
    List<OrderItem> findOrderItemsByOrderIdAndSellerId(Long orderId, String sellerId);
    List<OrderItem> findOrderItemsByOrderId(Long orderId);
}
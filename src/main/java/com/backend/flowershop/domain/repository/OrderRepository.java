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

    // ✅ 通用查询
    Optional<Order> findById(Long id);

    // 保留旧的查询，为了兼容性
    Order findById(Long orderId, String userId);

    List<OrderItem> findOrderItemsByUserId(String userId);

    // Seller Methods
    List<SellerOrderDTOResponse> findOrdersBySellerId(String sellerId);

    // ✅ [CHANGE] 参数类型改为 OrderStatus
    void updateStatus(Long orderId, OrderStatus status);

    // [NEW] 混合订单支持：只更新特定卖家在特定订单下的商品状态
    void updateItemsStatusBySeller(Long orderId, String sellerId, OrderStatus status);

    // [NEW] 混合订单支持：检查该订单是否所有子项都已发货
    boolean isOrderFullyShipped(Long orderId);
}
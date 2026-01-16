package com.backend.flowershop.domain.repository;

import com.backend.flowershop.domain.model.Order;
import com.backend.flowershop.domain.model.OrderItem;
import java.util.List;

public interface OrderRepository {
    // 1. 保存订单主表并返回生成的 ID
    Long saveOrder(Order order);

    // 2. 批量保存订单详情项
    void saveOrderItems(List<OrderItem> items);

    // 3. 获取用户的所有订单 (按时间倒序)
    List<Order> findByUserId(String userId);

    // 4. 获取订单详情 (包含订单项)
    Order findById(Long orderId, String userId);

    List<OrderItem> findOrderItemsByUserId(String userId);
}
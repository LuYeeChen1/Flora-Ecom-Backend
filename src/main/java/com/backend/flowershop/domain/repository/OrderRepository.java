package com.backend.flowershop.domain.repository;

import com.backend.flowershop.domain.model.Order;
import com.backend.flowershop.domain.model.OrderItem;
import java.util.List;

public interface OrderRepository {
    Long saveOrder(Order order);
    void saveOrderItems(List<OrderItem> items);
    List<Order> findByUserId(String userId);
    Order findById(Long orderId, String userId);

    //  必须有这个方法支持 N+1 优化
    List<OrderItem> findOrderItemsByUserId(String userId);
}
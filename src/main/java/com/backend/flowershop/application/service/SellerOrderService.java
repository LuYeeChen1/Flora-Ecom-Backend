package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.response.SellerOrderDTOResponse;
import com.backend.flowershop.domain.enums.OrderStatus;
import com.backend.flowershop.domain.model.OrderItem;
import com.backend.flowershop.domain.repository.FlowerRepository;
import com.backend.flowershop.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SellerOrderService {

    private final OrderRepository orderRepository;
    private final FlowerRepository flowerRepository;

    public SellerOrderService(OrderRepository orderRepository, FlowerRepository flowerRepository) {
        this.orderRepository = orderRepository;
        this.flowerRepository = flowerRepository;
    }

    public List<SellerOrderDTOResponse> getIncomingOrders(String sellerId) {
        return orderRepository.findOrdersBySellerId(sellerId);
    }

    @Transactional
    public void shipOrder(Long orderId, String sellerId) {
        orderRepository.updateItemsStatusBySeller(orderId, sellerId, OrderStatus.SHIPPED);
        boolean isFullyShipped = orderRepository.isOrderFullyShipped(orderId);
        if (isFullyShipped) {
            orderRepository.updateStatus(orderId, OrderStatus.SHIPPED);
        }
    }

    /**
     * [卖家功能] 审核买家的取消申请
     */
    @Transactional
    public void auditCancellation(Long orderId, String sellerId, boolean approved) {
        if (approved) {
            // 1. 同意 -> 更新主状态为 CANCELLED (MVP简化处理)
            orderRepository.updateStatus(orderId, OrderStatus.CANCELLED);

            // 2. 更新该卖家的子项状态
            orderRepository.updateItemsStatusBySeller(orderId, sellerId, OrderStatus.CANCELLED);

            // 仅查找属于该卖家的 OrderItem，防止误回滚其他卖家的库存
            List<OrderItem> items = orderRepository.findOrderItemsByOrderIdAndSellerId(orderId, sellerId);

            for (OrderItem item : items) {
                flowerRepository.restoreStock(item.getFlowerId(), item.getQuantity());
            }

            // TODO: 调用 PaymentService 执行退款

        } else {
            // 拒绝 -> 恢复为 PAID
            orderRepository.updateStatus(orderId, OrderStatus.PAID);
        }
    }

    @Transactional
    public void forceCancel(Long orderId, String sellerId) {
        // 更新状态
        orderRepository.updateStatus(orderId, OrderStatus.CANCELLED);
        orderRepository.updateItemsStatusBySeller(orderId, sellerId, OrderStatus.CANCELLED);

        // ✅ [安全修复] 安全回滚库存
        List<OrderItem> items = orderRepository.findOrderItemsByOrderIdAndSellerId(orderId, sellerId);
        for (OrderItem item : items) {
            flowerRepository.restoreStock(item.getFlowerId(), item.getQuantity());
        }
    }
}
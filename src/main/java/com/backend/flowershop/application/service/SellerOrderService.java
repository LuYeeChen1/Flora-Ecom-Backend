package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.response.SellerOrderDTOResponse;
import com.backend.flowershop.domain.enums.OrderStatus;
import com.backend.flowershop.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SellerOrderService {

    private final OrderRepository orderRepository;

    public SellerOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * 获取卖家的待处理订单
     * 这里展示的是混合视图：虽然是一个 Order ID，但 DTO 内部应该只包含该卖家的 items
     */
    public List<SellerOrderDTOResponse> getIncomingOrders(String sellerId) {
        return orderRepository.findOrdersBySellerId(sellerId);
    }

    /**
     * 卖家发货 (支持混合订单)
     * 逻辑：
     * 1. 只将该订单中属于当前卖家的商品标记为 SHIPPED。
     * 2. 检查该订单是否所有卖家的商品都已发货。
     * 3. 如果全部已发，将主订单状态更新为 SHIPPED；否则主订单保持 PAID。
     * * @param orderId 订单ID
     * @param sellerId 当前操作的卖家ID (用于区分混合订单中的商品归属)
     */
    @Transactional
    public void shipOrder(Long orderId, String sellerId) {
        // 1. 子商品层级：只发货属于当前卖家的部分
        // 例如：订单里有 A卖家的花 和 B卖家的花。A点击发货，只会把 A的花改为 SHIPPED。
        orderRepository.updateItemsStatusBySeller(orderId, sellerId, OrderStatus.SHIPPED);

        // 2. 主订单层级：检查是否达到 "全单发货" 的条件
        // 数据库会查询是否还存在状态为 PAID 的子项
        boolean isFullyShipped = orderRepository.isOrderFullyShipped(orderId);

        if (isFullyShipped) {
            // 只有当所有子项都发货了，主订单才变成 SHIPPED
            orderRepository.updateStatus(orderId, OrderStatus.SHIPPED);
        } else {
            // 可选：如果是部分发货，这里可以什么都不做 (保持 PAID)，
            // 或者如果您定义了 PARTIALLY_SHIPPED 状态，可以在这里更新。
            // 目前保持 MVP 状态定义，不动主状态。
        }
    }

    /**
     * [卖家功能] 审核买家的取消申请
     * @param approved true=同意取消(终结订单), false=拒绝(恢复订单)
     */
    @Transactional
    public void auditCancellation(Long orderId, String sellerId, boolean approved) {
        // 1. 验证权限 (混合订单场景下，通常需检查该 Seller 是否关联此订单，此处简化为直接操作)
        // Order order = ... check seller ownership

        if (approved) {
            // 同意 -> 变为 CANCELLED
            // TODO: 调用 PaymentService 执行退款
            // TODO: 调用 FlowerRepository 恢复库存
            orderRepository.updateStatus(orderId, OrderStatus.CANCELLED);

            // 级联更新所有子项状态
            orderRepository.updateItemsStatusBySeller(orderId, sellerId, OrderStatus.CANCELLED);
        } else {
            // 拒绝 -> 恢复为 PAID (商家可以继续发货)
            orderRepository.updateStatus(orderId, OrderStatus.PAID);
        }
    }

    /**
     * [卖家功能] 商家主动强制取消 (例如缺货)
     */
    @Transactional
    public void forceCancel(Long orderId, String sellerId) {
        // 只有未发货的订单可以强取消
        // 真实场景可能需要检查是否 PAID 或 CANCELLATION_REQUESTED
        orderRepository.updateStatus(orderId, OrderStatus.CANCELLED);

        // 标记该卖家的子项为 CANCELLED
        orderRepository.updateItemsStatusBySeller(orderId, sellerId, OrderStatus.CANCELLED);

        // TODO: 触发自动退款
    }
}
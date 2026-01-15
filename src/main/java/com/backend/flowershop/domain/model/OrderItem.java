package com.backend.flowershop.domain.model;

import java.math.BigDecimal;

public class OrderItem {
    private Long id;
    private Long orderId;             // 所属订单 ID
    private Long flowerId;            // 鲜花 ID
    private String flowerName;        // 下单时的鲜花名称
    private BigDecimal priceAtPurchase; // 下单时的单价 (快照)
    private int quantity;             // 数量

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getFlowerId() { return flowerId; }
    public void setFlowerId(Long flowerId) { this.flowerId = flowerId; }

    public String getFlowerName() { return flowerName; }
    public void setFlowerName(String flowerName) { this.flowerName = flowerName; }

    public BigDecimal getPriceAtPurchase() { return priceAtPurchase; }
    public void setPriceAtPurchase(BigDecimal priceAtPurchase) { this.priceAtPurchase = priceAtPurchase; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
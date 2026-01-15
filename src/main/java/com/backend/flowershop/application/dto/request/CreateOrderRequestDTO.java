package com.backend.flowershop.application.dto.request;

public class CreateOrderRequestDTO {
    private String shippingAddress;

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
}
package com.backend.flowershop.application.service;

import com.backend.flowershop.application.dto.request.CartItemDTORequest;
import com.backend.flowershop.application.dto.response.CartItemDTOResponse;
import com.backend.flowershop.domain.model.CartItem;
import com.backend.flowershop.domain.repository.CartRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;

    // 注入 S3 域名，用于拼接完整的图片链接
    @Value("${aws.s3.base-url:https://flower-shop-product.s3.us-east-1.amazonaws.com/}")
    private String s3BaseUrl;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    // 1. 添加到购物车
    public void addToCart(String userId, CartItemDTORequest request) {
        // 简单的校验
        if (request.quantity() == null || request.quantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        cartRepository.addToCart(userId, request.flowerId(), request.quantity());
    }

    // 2. 获取我的购物车 (Entity -> DTO 转换)
    public List<CartItemDTOResponse> getMyCart(String userId) {
        List<CartItem> items = cartRepository.findAllByUserId(userId);

        return items.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // 3. 移除商品
    public void removeFromCart(String userId, Long cartId) {
        cartRepository.deleteCartItem(cartId, userId);
    }

    public void updateCartItemQuantity(String userId, Long cartId, int quantity) {
        if (quantity <= 0) {
            // 如果数量减到0，直接移除商品
            cartRepository.deleteCartItem(cartId, userId);
        } else {
            cartRepository.updateQuantity(cartId, userId, quantity);
        }
    }

    // --- Mapper Helper ---
    private CartItemDTOResponse mapToDTO(CartItem item) {
        // 拼接图片完整 URL
        String fullUrl = item.getFlowerImageUrl();
        if (fullUrl != null && !fullUrl.startsWith("http")) {
            fullUrl = s3BaseUrl + fullUrl;
        }

        // 计算小计
        double subtotal = item.getFlowerPrice() * item.getQuantity();

        return new CartItemDTOResponse(
                item.getId(),
                item.getFlowerId(),
                item.getFlowerName(),
                item.getFlowerPrice(),
                fullUrl,
                item.getQuantity(),
                subtotal
        );
    }
}
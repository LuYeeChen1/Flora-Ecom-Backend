package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.application.dto.request.CartItemDTORequest;
import com.backend.flowershop.application.dto.response.CartItemDTOResponse;
import com.backend.flowershop.application.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // 1. 添加商品: POST /api/cart
    @PostMapping
    public ResponseEntity<?> addToCart(
            @AuthenticationPrincipal JwtAuthenticationToken token,
            @RequestBody CartItemDTORequest request) {

        String userId = token.getName(); // 获取 Cognito Sub (User ID)
        cartService.addToCart(userId, request);

        return ResponseEntity.ok(Map.of("message", "Item added to cart"));
    }

    // 2. 查看购物车: GET /api/cart
    @GetMapping
    public ResponseEntity<List<CartItemDTOResponse>> getMyCart(
            @AuthenticationPrincipal JwtAuthenticationToken token) {

        String userId = token.getName();
        return ResponseEntity.ok(cartService.getMyCart(userId));
    }

    // 3. 移除商品: DELETE /api/cart/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFromCart(
            @AuthenticationPrincipal JwtAuthenticationToken token,
            @PathVariable Long id) {

        String userId = token.getName();
        cartService.removeFromCart(userId, id);

        return ResponseEntity.ok(Map.of("message", "Item removed"));
    }
}
package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.application.dto.request.CreateOrderRequestDTO;
import com.backend.flowershop.application.service.OrderService;
import com.backend.flowershop.domain.model.Order; // 修正：引入正确的实体类，而不是 Spring 的注解
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 提交订单: POST /api/orders/checkout
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody CreateOrderRequestDTO request) {

        // 防御性检查
        if (jwt == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            String userId = jwt.getSubject();
            Long orderId = orderService.checkout(userId, request);

            return ResponseEntity.ok(Map.of(
                    "message", "Order placed successfully",
                    "orderId", orderId
            ));
        } catch (RuntimeException e) {
            // 捕获库存不足或空购物车异常，返回 400 Bad Request
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // 获取我的订单: GET /api/orders
    @GetMapping
    public ResponseEntity<List<Order>> getMyOrders(@AuthenticationPrincipal Jwt jwt) {
        if (jwt == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String userId = jwt.getSubject();
        List<Order> orders = orderService.getUserOrders(userId);

        return ResponseEntity.ok(orders);
    }
}
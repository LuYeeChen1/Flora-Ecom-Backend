package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.application.dto.response.SellerOrderDTOResponse;
import com.backend.flowershop.application.service.SellerOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/seller/orders")
public class SellerOrderController {

    private final SellerOrderService sellerOrderService;

    public SellerOrderController(SellerOrderService sellerOrderService) {
        this.sellerOrderService = sellerOrderService;
    }

    @GetMapping
    public ResponseEntity<List<SellerOrderDTOResponse>> getOrders(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(sellerOrderService.getIncomingOrders(jwt.getSubject()));
    }

    @PatchMapping("/orders/{orderId}/ship")
    public ResponseEntity<?> shipOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal Jwt jwt) {

        // 从 Token 获取 Seller ID
        String sellerId = jwt.getSubject();

        // 调用更新后的服务
        sellerOrderService.shipOrder(orderId, sellerId);

        return ResponseEntity.ok().build();
    }

    // 审核取消申请
    @PostMapping("/orders/{orderId}/audit-cancel")
    public ResponseEntity<?> auditCancel(
            @PathVariable Long orderId,
            @RequestBody Map<String, Boolean> body, // { "approved": true }
            @AuthenticationPrincipal Jwt jwt) {

        sellerOrderService.auditCancellation(orderId, jwt.getSubject(), body.get("approved"));
        return ResponseEntity.ok(Map.of("message", "Audit processed"));
    }

    // 强制取消
    @PostMapping("/orders/{orderId}/force-cancel")
    public ResponseEntity<?> forceCancel(@PathVariable Long orderId, @AuthenticationPrincipal Jwt jwt) {
        sellerOrderService.forceCancel(orderId, jwt.getSubject());
        return ResponseEntity.ok(Map.of("message", "Order force cancelled"));
    }
}
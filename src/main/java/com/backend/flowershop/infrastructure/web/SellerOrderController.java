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

    /**
     * 获取卖家的所有订单列表
     */
    @GetMapping
    public ResponseEntity<List<SellerOrderDTOResponse>> getOrders(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(sellerOrderService.getIncomingOrders(jwt.getSubject()));
    }

    /**
     * ✅ [新增] 获取单个订单详情
     * 用于前端点击 "View Detail" 时调用
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<SellerOrderDTOResponse> getOrderDetails(
            @PathVariable Long orderId,
            @AuthenticationPrincipal Jwt jwt) {
        // 调用 Service 层的 getOrderDetails 方法 (请确保 Service 层已更新)
        return ResponseEntity.ok(sellerOrderService.getOrderDetails(orderId, jwt.getSubject()));
    }

    /**
     * 发货操作
     */
    @PatchMapping("/{orderId}/ship")
    public ResponseEntity<?> shipOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal Jwt jwt) {

        String sellerId = jwt.getSubject();
        sellerOrderService.shipOrder(orderId, sellerId);

        return ResponseEntity.ok().build();
    }

    /**
     * 审核取消申请 (同意/拒绝)
     */
    @PostMapping("/{orderId}/audit-cancel")
    public ResponseEntity<?> auditCancel(
            @PathVariable Long orderId,
            @RequestBody Map<String, Boolean> body, // Expects: { "approved": true }
            @AuthenticationPrincipal Jwt jwt) {

        sellerOrderService.auditCancellation(orderId, jwt.getSubject(), body.get("approved"));
        return ResponseEntity.ok(Map.of("message", "Audit processed"));
    }

    /**
     * 强制取消订单
     */
    @PostMapping("/{orderId}/force-cancel")
    public ResponseEntity<?> forceCancel(@PathVariable Long orderId, @AuthenticationPrincipal Jwt jwt) {
        sellerOrderService.forceCancel(orderId, jwt.getSubject());
        return ResponseEntity.ok(Map.of("message", "Order force cancelled"));
    }
}
package com.backend.flowershop.infrastructure.web;

import com.backend.flowershop.application.dto.request.SellerApplyDTORequest;
import com.backend.flowershop.application.service.SellerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seller")
public class SellerController {

    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    /**
     * 提交卖家申请
     * Endpoint: POST /api/seller/apply
     * Header: Authorization: Bearer <token>
     */
    @PostMapping("/apply")
    public ResponseEntity<String> applyForSeller(
            @AuthenticationPrincipal Jwt jwt, // 自动注入当前登录用户的 Token 信息
            @Valid @RequestBody SellerApplyDTORequest request
    ) {
        // 1. 安全获取 User ID (Cognito 'sub' 字段)
        // 这样可以确保用户只能给自己申请，不能伪造他人 ID
        String userId = jwt.getClaimAsString("sub");

        if (userId == null) {
            return ResponseEntity.status(401).body("Invalid Token: User ID not found.");
        }

        // 2. 调用业务服务
        sellerService.applyForSeller(userId, request);

        return ResponseEntity.ok("Seller application submitted successfully.");
    }
}
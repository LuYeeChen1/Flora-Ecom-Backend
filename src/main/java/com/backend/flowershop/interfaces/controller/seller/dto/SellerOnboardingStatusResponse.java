package com.backend.flowershop.interfaces.controller.seller.dto;

/**
 * 作用：Seller Onboarding 状态返回 DTO
 * - dbStatus：本地业务库状态
 * - roleStage：JWT 中 custom:role_stage（冻结事实）
 */
public record SellerOnboardingStatusResponse(
        String userSub,
        String dbStatus,
        String roleStage
) {}

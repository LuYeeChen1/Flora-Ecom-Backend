package com.backend.flowershop.domain.model.seller;

/**
 * 作用：本地业务库的 Seller Onboarding 状态（最小）
 * 说明：可与 Cognito role_stage 做对照，但不写在 Domain 里耦合 Cognito
 */
public enum SellerOnboardingStatus {
    SELLER_PENDING,
    SELLER_REVIEWING,
    SELLER_REJECTED,
    SELLER_APPROVED,
    SELLER_ACTIVE
}

package com.backend.flowershop.domain.model.auth;

/**
 * 作用：Seller 状态机（冻结）
 */
public enum RoleStage {
    CUSTOMER,
    SELLER_PENDING,
    SELLER_REVIEWING,
    SELLER_REJECTED,
    SELLER_APPROVED,
    SELLER_ACTIVE
}

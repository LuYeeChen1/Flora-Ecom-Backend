package com.backend.flowershop.domain.model.seller;

/**
 * SellerStage：卖家资料流程状态（冻结文件）

 * 职责边界：
 * 1) 表达“SellerProfile 在业务数据库内的流程状态”
 * 2) 不依赖 Cognito，不依赖 JWT，不依赖 Spring
 * 3) 可与 RoleStage 对齐，但用于不同存储：
 *    - RoleStage：Cognito custom:role_stage
 *    - SellerStage：本地 MySQL（SellerProfile.stage）

 *  冻结约束：
 * - 枚举值名称只增不改（避免数据库历史数据不兼容）
 */
public enum SellerStage {

    CUSTOMER,

    SELLER_PENDING,
    SELLER_REVIEWING,
    SELLER_REJECTED,
    SELLER_APPROVED,
    SELLER_ACTIVE
}

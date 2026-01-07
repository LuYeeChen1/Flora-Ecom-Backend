package com.backend.flowershop.domain.model.user;

/**
 * RoleStage：用户角色阶段（冻结文件）

 * 职责边界：
 * 1) 领域层对“用户当前阶段状态”的统一表达
 * 2) 与 Cognito custom:role_stage 完全对齐（字符串值必须一致）
 * 3) 只表达“状态”，不包含状态流转规则（流转规则由 SellerStagePolicy 负责）

 * ️ 冻结约束：
 * - 枚举值名称 == Cognito 中存储的字符串值
 * - 只能新增，不允许修改或重命名（否则会破坏已存在用户数据）

 * Seller 状态机说明：
 * - CUSTOMER：普通用户（默认）
 * - SELLER_PENDING：已提交 Seller 申请，等待处理
 * - SELLER_REVIEWING：平台审核中
 * - SELLER_REJECTED：审核失败
 * - SELLER_APPROVED：审核通过（尚未激活）
 * - SELLER_ACTIVE：唯一允许加入 SELLER Group 的状态
 */
public enum RoleStage {

    CUSTOMER,

    SELLER_PENDING,
    SELLER_REVIEWING,
    SELLER_REJECTED,
    SELLER_APPROVED,
    SELLER_ACTIVE
}

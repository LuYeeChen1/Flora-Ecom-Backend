package com.backend.flowershop.domain.service;

import com.backend.flowershop.domain.model.seller.SellerStage;
import com.backend.flowershop.domain.model.user.RoleStage;

/**
 * SellerStagePolicy：卖家状态机策略（冻结文件）

 * 职责边界：
 * 1) 只定义“状态是否允许流转”的领域规则（纯函数）
 * 2) 不依赖数据库、不依赖 Spring、不依赖 Cognito SDK
 * 3) application/rules 可以调用本策略来判断合法性

 * 为什么在 Domain：
 * - 这是业务核心规则（状态机），应该集中、可测试、可复用

 * 注意：
 * - 你冻结了两套枚举：RoleStage（Cognito）与 SellerStage（业务库）
 * - 这里同时提供两套判断，保持对齐但不强耦合
 */
public final class SellerStagePolicy {

    /**
     * 判断 SellerStage 是否允许从 from -> to
     */
    public boolean canTransition(SellerStage from, SellerStage to) {
        if (from == null || to == null) return false;

        // 冻结状态机：仅允许按“申请→审核→通过/拒绝→激活”推进
        switch (from) {
            case CUSTOMER:
                return to == SellerStage.SELLER_PENDING;

            case SELLER_PENDING:
                return to == SellerStage.SELLER_REVIEWING;

            case SELLER_REVIEWING:
                return to == SellerStage.SELLER_APPROVED || to == SellerStage.SELLER_REJECTED;

            case SELLER_APPROVED:
                return to == SellerStage.SELLER_ACTIVE;

            case SELLER_REJECTED:
                // 被拒后是否允许重新申请：冻结为允许回到 PENDING（可按你未来需求改为禁止，但那是“修改规则”，要慎重）
                return to == SellerStage.SELLER_PENDING;

            case SELLER_ACTIVE:
                // 激活后不允许在业务库里降级/回退（若未来要支持封禁/停用，应新增状态而不是复用旧状态）
                return false;

            default:
                return false;
        }
    }

    /**
     * 判断 RoleStage（Cognito custom:role_stage）是否允许从 from -> to
     */
    public boolean canTransition(RoleStage from, RoleStage to) {
        if (from == null || to == null) return false;

        switch (from) {
            case CUSTOMER:
                return to == RoleStage.SELLER_PENDING;

            case SELLER_PENDING:
                return to == RoleStage.SELLER_REVIEWING;

            case SELLER_REVIEWING:
                return to == RoleStage.SELLER_APPROVED || to == RoleStage.SELLER_REJECTED;

            case SELLER_APPROVED:
                return to == RoleStage.SELLER_ACTIVE;

            case SELLER_REJECTED:
                return to == RoleStage.SELLER_PENDING;

            case SELLER_ACTIVE:
                return false;

            default:
                return false;
        }
    }
}

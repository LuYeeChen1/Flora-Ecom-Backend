package com.backend.flowershop.shared.error;

/**
 * ForbiddenException：权限不足异常（冻结文件）

 * 职责边界：
 * 1) 表示“用户已登录，但业务/权限不允许当前操作”
 * 2) 统一映射为 HTTP 403
 * 3) 不用于未登录（那是 UNAUTHENTICATED 的职责）

 * 典型使用场景：
 * - 当前用户不是 CUSTOMER，却尝试发起 Seller Onboarding
 * - 当前用户不是 ADMIN，却尝试执行管理员操作

 * 注意：
 * - 鉴权判断应集中在 Application Service / AuthorizationPort
 * - Controller 不应直接判断角色
 */
public class ForbiddenException extends DomainException {

    public ForbiddenException(String message) {
        super(ErrorCode.FORBIDDEN, message);
    }
}

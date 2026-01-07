package com.backend.flowershop.shared.error;

/**
 * ErrorCode：全局错误码（冻结文件）

 * 职责边界：
 * 1) 只定义“错误类型枚举”，不携带业务逻辑
 * 2) 用于异常（DomainException 等）与 API 错误响应（ApiErrorResponse）之间的稳定契约
 * 3) 后续新增错误码：只能新增枚举值，不要改已有名字（避免前端/日志/测试断裂）

 * 输入：无
 * 输出：错误码枚举值
 */
public enum ErrorCode {

    // ========== 通用 ==========
    UNKNOWN_ERROR,
    VALIDATION_FAILED,
    FORBIDDEN,
    NOT_FOUND,

    // ========== 认证/鉴权 ==========
    UNAUTHENTICATED,
    INSUFFICIENT_ROLE,

    // ========== Seller Onboarding ==========
    SELLER_STAGE_INVALID,
    SELLER_PROFILE_INCOMPLETE,
    SELLER_DOCUMENT_INVALID
}

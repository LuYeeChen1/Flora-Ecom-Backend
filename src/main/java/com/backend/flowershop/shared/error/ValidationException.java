package com.backend.flowershop.shared.error;

/**
 * ValidationException：输入校验异常（冻结文件）

 * 职责边界：
 * 1) 仅用于“输入数据不合法”（格式/缺失/长度/非法值）
 * 2) 不用于业务状态判断（那是 Rule / DomainException 的职责）
 * 3) 统一映射为 HTTP 400（由 GlobalExceptionHandler 处理）

 * 典型触发位置：
 * - application/validator/*
 * - normalize 之后、进入 Rule Pipeline 之前

 * 与 DomainException 的区别：
 * - ValidationException：用户“填错了”
 * - DomainException：用户“不能这样做”
 */
public class ValidationException extends DomainException {

    public ValidationException(String message) {
        super(ErrorCode.VALIDATION_FAILED, message);
    }
}

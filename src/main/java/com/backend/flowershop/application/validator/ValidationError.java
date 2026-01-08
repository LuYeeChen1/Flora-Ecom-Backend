package com.backend.flowershop.application.validator;

/**
 * 作用：统一校验错误结构
 * 注意：validate 逻辑不在这里；这里只承载结果
 */
public record ValidationError(
        String code,
        String field,
        String message
) {}

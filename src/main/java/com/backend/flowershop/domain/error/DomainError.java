package com.backend.flowershop.domain.error;

/**
 * 作用：领域错误（最小）
 */
public record DomainError(
        String code,
        String message
) {}

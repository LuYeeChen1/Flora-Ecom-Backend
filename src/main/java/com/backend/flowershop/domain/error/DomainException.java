package com.backend.flowershop.domain.error;

/**
 * 作用：领域异常（最小）
 * 说明：后续可统一用 @ControllerAdvice 转成标准错误响应
 */
public class DomainException extends RuntimeException {

    private final String code;

    public DomainException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String code() {
        return code;
    }
}

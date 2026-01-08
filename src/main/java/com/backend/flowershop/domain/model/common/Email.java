package com.backend.flowershop.domain.model.common;

/**
 * 作用：Email 值对象（最小实现）
 * 边界：不依赖 Spring/DB/AWS
 */
public final class Email {

    private final String value;

    public Email(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}

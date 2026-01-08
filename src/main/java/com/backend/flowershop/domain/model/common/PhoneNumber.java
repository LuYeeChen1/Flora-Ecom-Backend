package com.backend.flowershop.domain.model.common;

/**
 * 作用：PhoneNumber 值对象（最小实现）
 */
public final class PhoneNumber {

    private final String value;

    public PhoneNumber(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}

package com.backend.flowershop.domain.model.common;

/**
 * 作用：地址值对象（最小实现）
 */
public record Address(
        String line1,
        String line2,
        String city,
        String state,
        String postcode,
        String country
) {}

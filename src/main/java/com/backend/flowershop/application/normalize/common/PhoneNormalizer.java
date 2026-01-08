package com.backend.flowershop.application.normalize.common;

import org.springframework.stereotype.Component;

/**
 * 作用：电话标准化（Normalize only）
 * 规则：trim + 去掉空格
 * 边界：不做合法性校验（格式校验在 rule impl）
 */
@Component
public class PhoneNormalizer {

    private final TextNormalizer textNormalizer;

    public PhoneNormalizer(TextNormalizer textNormalizer) {
        this.textNormalizer = textNormalizer;
    }

    public String normalize(String phone) {
        String s = textNormalizer.normalize(phone);
        if (s == null) return null;
        return s.replace(" ", "");
    }
}

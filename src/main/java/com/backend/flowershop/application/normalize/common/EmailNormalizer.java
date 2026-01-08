package com.backend.flowershop.application.normalize.common;

import org.springframework.stereotype.Component;

/**
 * 作用：Email 标准化（Normalize only）
 * 边界：不做合法性校验
 */
@Component
public class EmailNormalizer {

    private final TextNormalizer textNormalizer;

    public EmailNormalizer(TextNormalizer textNormalizer) {
        this.textNormalizer = textNormalizer;
    }

    public String normalize(String email) {
        String s = textNormalizer.normalize(email);
        if (s == null) return null;
        return s.toLowerCase();
    }
}

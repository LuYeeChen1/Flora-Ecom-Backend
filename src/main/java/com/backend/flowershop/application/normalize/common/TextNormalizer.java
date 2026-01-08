package com.backend.flowershop.application.normalize.common;

import org.springframework.stereotype.Component;

/**
 * 作用：最小文本标准化（Normalize only）
 * 规则：trim + 将空白压缩为单空格
 * 边界：不做合法性校验
 */
@Component
public class TextNormalizer {

    public String normalize(String input) {
        if (input == null) return null;
        String s = input.trim();
        if (s.isEmpty()) return "";
        return s.replaceAll("\\s+", " ");
    }
}

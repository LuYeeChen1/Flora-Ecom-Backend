package com.backend.flowershop.domain.model.common;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * PhoneNumber：电话号码值对象（冻结文件）

 * 职责边界：
 * 1) 领域层表达“电话号码”的语义与不可变性
 * 2) 只做最小、稳定的合法性约束（避免过度严格）
 * 3) 不负责国家码推断、不负责格式化展示（这些属于 Normalize/Validator）

 * 约束策略（最小可用）：
 * - 去除空白后必须以 '+' 或数字开头
 * - 仅允许：数字、'+'、'-'、空格、括号
 * - 至少包含 7 位数字（粗略防止明显无效）
 */
public final class PhoneNumber {

    private static final Pattern ALLOWED =
            Pattern.compile("^[+0-9][0-9\\-\\s()]*$");

    private final String value;

    public PhoneNumber(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("PhoneNumber 不能为空");
        }

        String trimmed = value.trim();

        if (!ALLOWED.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("PhoneNumber 含有非法字符");
        }

        // 统计数字位数（最小约束）
        int digits = 0;
        for (int i = 0; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);
            if (c >= '0' && c <= '9') digits++;
        }
        if (digits < 7) {
            throw new IllegalArgumentException("PhoneNumber 位数不足");
        }

        this.value = trimmed;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneNumber)) return false;
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

package com.backend.flowershop.domain.model.common;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Email：邮箱值对象（冻结文件）

 * 职责边界：
 * 1) 领域层表达“邮箱”这种值的语义与不可变性
 * 2) 只做最小、稳定的格式约束（避免过度严格导致合法邮箱被拒）
 * 3) 不依赖 Spring Validation，不依赖任何外部库

 * 注意：
 * - 更复杂的校验（如 DNS/MX）属于基础设施或外部服务，不在 Domain 做

 * 输入：
 * - value：非空、非空白、基本格式符合

 * 输出：
 * - 不可变对象（equals/hashCode 基于标准化后的值）
 */
public final class Email {

    // 最小邮箱格式：something@something.something
    private static final Pattern BASIC =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private final String value;

    public Email(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email 不能为空");
        }

        // 标准化：trim + 小写（邮箱本地部分理论可区分大小写，但实际系统通常按不区分处理）
        String normalized = value.trim().toLowerCase();

        if (!BASIC.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Email 格式不合法");
        }

        this.value = normalized;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
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

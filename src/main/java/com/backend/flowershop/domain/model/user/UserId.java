package com.backend.flowershop.domain.model.user;

import java.util.Objects;

/**
 * UserId：用户 ID 值对象（冻结文件）

 * 职责边界：
 * 1) 只表达“用户在业务域内的标识”
 * 2) 不关心它来自哪里（数据库自增 / UUID / 其他）
 * 3) 领域层用值对象包起来，避免到处传裸 String 导致混用

 * 输入：
 * - value：非空、非空白字符串

 * 输出：
 * - 不可变对象（equals/hashCode 基于 value）
 */
public final class UserId {

    private final String value;

    public UserId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("UserId 不能为空");
        }
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserId)) return false;
        UserId userId = (UserId) o;
        return Objects.equals(value, userId.value);
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

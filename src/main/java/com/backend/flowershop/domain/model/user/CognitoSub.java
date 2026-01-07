package com.backend.flowershop.domain.model.user;

import java.util.Objects;

/**
 * CognitoSub：Cognito 用户 sub 值对象（冻结文件）

 * 职责边界：
 * 1) 表达来自 Cognito/JWT 的用户唯一标识（sub）
 * 2) 与业务内部 UserId 分离（避免概念混用）
 * 3) Domain 层只认“这是一个外部身份锚点”，不依赖 JWT/Spring

 * 输入：
 * - value：非空、非空白字符串

 * 输出：
 * - 不可变对象（equals/hashCode 基于 value）
 */
public final class CognitoSub {

    private final String value;

    public CognitoSub(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("CognitoSub 不能为空");
        }
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CognitoSub)) return false;
        CognitoSub that = (CognitoSub) o;
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

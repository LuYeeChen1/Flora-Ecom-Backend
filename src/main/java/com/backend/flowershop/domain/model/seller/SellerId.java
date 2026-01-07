package com.backend.flowershop.domain.model.seller;

import java.util.Objects;

/**
 * SellerId：卖家 ID 值对象（冻结文件）

 * 职责边界：
 * 1) 表达“卖家在业务域内的标识”
 * 2) 不关心它如何生成（UUID / DB 自增 / 其他）
 * 3) 领域层用值对象封装，避免到处传裸 String 导致混用

 * 输入：
 * - value：非空、非空白字符串

 * 输出：
 * - 不可变对象（equals/hashCode 基于 value）
 */
public final class SellerId {

    private final String value;

    public SellerId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("SellerId 不能为空");
        }
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SellerId)) return false;
        SellerId sellerId = (SellerId) o;
        return Objects.equals(value, sellerId.value);
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

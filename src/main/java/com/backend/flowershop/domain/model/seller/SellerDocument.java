package com.backend.flowershop.domain.model.seller;

import java.util.Objects;

/**
 * SellerDocument：卖家提交文件（冻结文件）

 * 职责边界：
 * 1) 领域层只保存“文件引用与元信息”，不保存文件内容
 * 2) 不依赖 S3 / 本地文件系统 / AWS SDK
 * 3) 校验（格式/大小/类型）由 application/rules 与 validator 处理

 * 典型用途：
 * - 身份证明、营业执照、地址证明等（实际文件可存 S3/本地，Domain 只存 key/url）

 * 字段说明（最小集合）：
 * - type：文件类型（业务定义，例如 "BUSINESS_LICENSE"）
 * - storageKey：文件存储引用（例如 S3 key / 本地路径 / 未来对象存储 key）
 * - originalName：原始文件名（可选，用于展示）
 */
public final class SellerDocument {

    private final String type;
    private final String storageKey;
    private final String originalName;

    public SellerDocument(String type, String storageKey, String originalName) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("SellerDocument.type 不能为空");
        }
        if (storageKey == null || storageKey.isBlank()) {
            throw new IllegalArgumentException("SellerDocument.storageKey 不能为空");
        }

        this.type = type.trim();
        this.storageKey = storageKey.trim();
        this.originalName = (originalName == null) ? null : originalName.trim();
    }

    public String type() { return type; }
    public String storageKey() { return storageKey; }
    public String originalName() { return originalName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SellerDocument)) return false;
        SellerDocument that = (SellerDocument) o;
        return Objects.equals(type, that.type)
                && Objects.equals(storageKey, that.storageKey)
                && Objects.equals(originalName, that.originalName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, storageKey, originalName);
    }

    @Override
    public String toString() {
        return "SellerDocument{" +
                "type='" + type + '\'' +
                ", storageKey='" + storageKey + '\'' +
                ", originalName='" + originalName + '\'' +
                '}';
    }
}

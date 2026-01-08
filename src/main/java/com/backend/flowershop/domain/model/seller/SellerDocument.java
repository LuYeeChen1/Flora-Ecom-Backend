package com.backend.flowershop.domain.model.seller;

/**
 * 作用：Seller 文件/证件（最小字段）
 */
public record SellerDocument(
        String type,
        String number,
        String url
) {}

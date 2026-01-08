package com.backend.flowershop.interfaces.controller.seller.dto;

import java.util.List;

/**
 * 作用：提交/更新 Seller Onboarding 请求 DTO
 * 边界：仅承载输入数据，不做校验（validate 必须在 rule impl）
 */
public record SubmitSellerOnboardingRequest(
        String companyName,
        String phone,
        String addressLine1,
        String addressLine2,
        String city,
        String state,
        String postcode,
        String country,
        List<SellerDocumentItem> documents
) {
    public record SellerDocumentItem(
            String type,
            String number,
            String url
    ) {}
}

package com.backend.flowershop.interfaces.controller.seller.dto;

import java.util.List;

/**
 * 作用：Seller Onboarding 资料返回 DTO
 */
public record SellerOnboardingResponse(
        String userSub,
        String status,
        String companyName,
        String phone,
        AddressDto address,
        List<DocumentDto> documents
) {
    public record AddressDto(
            String line1,
            String line2,
            String city,
            String state,
            String postcode,
            String country
    ) {}

    public record DocumentDto(
            String type,
            String number,
            String url
    ) {}
}

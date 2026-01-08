package com.backend.flowershop.domain.model.seller;

import com.backend.flowershop.domain.model.common.Address;

/**
 * 作用：Seller 资料（最小字段）
 */
public record SellerProfile(
        String companyName,
        String phone,
        Address address
) {}

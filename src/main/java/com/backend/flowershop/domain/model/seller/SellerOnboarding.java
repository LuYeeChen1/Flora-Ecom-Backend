package com.backend.flowershop.domain.model.seller;

import java.time.Instant;
import java.util.List;

/**
 * 作用：Seller Onboarding 聚合根（最小闭环）
 */
public record SellerOnboarding(
        String userSub,
        SellerProfile profile,
        List<SellerDocument> documents,
        SellerOnboardingStatus status,
        Instant createdAt,
        Instant updatedAt
) {}

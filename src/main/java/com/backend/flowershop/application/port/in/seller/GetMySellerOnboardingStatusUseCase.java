package com.backend.flowershop.application.port.in.seller;

import com.backend.flowershop.interfaces.controller.seller.dto.SellerOnboardingStatusResponse;
import com.backend.flowershop.interfaces.security.AuthPrincipal;

/**
 * 作用：查询“我”的 Seller Onboarding 状态
 */
public interface GetMySellerOnboardingStatusUseCase {
    SellerOnboardingStatusResponse execute(AuthPrincipal principal);
}

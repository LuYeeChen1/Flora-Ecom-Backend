package com.backend.flowershop.application.port.in.seller;

import com.backend.flowershop.domain.model.seller.SellerOnboarding;
import com.backend.flowershop.interfaces.security.AuthPrincipal;

/**
 * 作用：查询“我”的 Seller Onboarding
 */
public interface GetMySellerOnboardingUseCase {
    SellerOnboarding execute(AuthPrincipal principal);
}

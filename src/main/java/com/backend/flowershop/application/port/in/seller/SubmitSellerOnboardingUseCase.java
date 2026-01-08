package com.backend.flowershop.application.port.in.seller;

import com.backend.flowershop.domain.model.seller.SellerDocument;
import com.backend.flowershop.domain.model.seller.SellerOnboarding;
import com.backend.flowershop.domain.model.seller.SellerProfile;
import com.backend.flowershop.interfaces.security.AuthPrincipal;

import java.util.List;

/**
 * 作用：提交 Seller Onboarding 用例入口
 * 边界：只定义契约
 */
public interface SubmitSellerOnboardingUseCase {
    SellerOnboarding execute(AuthPrincipal principal, SellerProfile profile, List<SellerDocument> documents);
}

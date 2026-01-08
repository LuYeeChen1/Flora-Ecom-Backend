package com.backend.flowershop.application.service.seller;

import com.backend.flowershop.application.port.in.seller.GetMySellerOnboardingUseCase;
import com.backend.flowershop.application.port.out.seller.SellerOnboardingRepository;
import com.backend.flowershop.domain.error.DomainException;
import com.backend.flowershop.domain.model.seller.SellerOnboarding;
import com.backend.flowershop.interfaces.security.AuthPrincipal;
import org.springframework.stereotype.Service;

/**
 * 作用：查询“我”的 Seller Onboarding
 */
@Service
public class GetMySellerOnboardingService implements GetMySellerOnboardingUseCase {

    private final SellerOnboardingRepository repository;

    public GetMySellerOnboardingService(SellerOnboardingRepository repository) {
        this.repository = repository;
    }

    @Override
    public SellerOnboarding execute(AuthPrincipal principal) {
        return repository.findByUserSub(principal.userSub())
                .orElseThrow(() -> new DomainException("SELLER_ONBOARDING_NOT_FOUND", "未找到 seller onboarding"));
    }
}

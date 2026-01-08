package com.backend.flowershop.application.service.seller;

import com.backend.flowershop.application.port.in.seller.GetMySellerOnboardingStatusUseCase;
import com.backend.flowershop.application.port.out.seller.SellerOnboardingRepository;
import com.backend.flowershop.domain.model.seller.SellerOnboarding;
import com.backend.flowershop.interfaces.controller.seller.dto.SellerOnboardingStatusResponse;
import com.backend.flowershop.interfaces.security.AuthPrincipal;
import org.springframework.stereotype.Service;

/**
 * 作用：查询“我”的 Seller Onboarding 状态
 * 输出：dbStatus + JWT roleStage 对照
 */
@Service
public class GetMySellerOnboardingStatusService implements GetMySellerOnboardingStatusUseCase {

    private final SellerOnboardingRepository repository;

    public GetMySellerOnboardingStatusService(SellerOnboardingRepository repository) {
        this.repository = repository;
    }

    @Override
    public SellerOnboardingStatusResponse execute(AuthPrincipal principal) {
        String dbStatus = repository.findByUserSub(principal.userSub())
                .map(SellerOnboarding::status)
                .map(Enum::name)
                .orElse("NONE");

        return new SellerOnboardingStatusResponse(
                principal.userSub(),
                dbStatus,
                principal.roleStage()
        );
    }
}

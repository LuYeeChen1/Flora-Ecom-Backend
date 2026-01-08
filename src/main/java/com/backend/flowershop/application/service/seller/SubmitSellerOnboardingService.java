package com.backend.flowershop.application.service.seller;

import com.backend.flowershop.application.port.in.seller.SubmitSellerOnboardingUseCase;
import com.backend.flowershop.application.port.out.clock.ClockPort;
import com.backend.flowershop.application.port.out.seller.SellerOnboardingRepository;
import com.backend.flowershop.application.validator.SellerOnboardingValidator;
import com.backend.flowershop.domain.error.DomainException;
import com.backend.flowershop.domain.model.seller.SellerDocument;
import com.backend.flowershop.domain.model.seller.SellerOnboarding;
import com.backend.flowershop.domain.model.seller.SellerOnboardingStatus;
import com.backend.flowershop.domain.model.seller.SellerProfile;
import com.backend.flowershop.interfaces.security.AuthPrincipal;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * 作用：提交 Seller Onboarding（最小闭环）
 * 流程：Validator(=Normalize + Pipeline) -> Save
 * 边界：不写 validate 逻辑
 */
@Service
public class SubmitSellerOnboardingService implements SubmitSellerOnboardingUseCase {

    private final SellerOnboardingValidator validator;
    private final SellerOnboardingRepository repository;
    private final ClockPort clock;

    public SubmitSellerOnboardingService(
            SellerOnboardingValidator validator,
            SellerOnboardingRepository repository,
            ClockPort clock
    ) {
        this.validator = validator;
        this.repository = repository;
        this.clock = clock;
    }

    @Override
    public SellerOnboarding execute(AuthPrincipal principal, SellerProfile profile, List<SellerDocument> documents) {
        SellerOnboardingValidator.Result r = validator.validateSubmit(principal, profile, documents);
        if (!r.validation().valid()) {
            throw new DomainException(
                    "VALIDATION_FAILED",
                    "校验失败：" + r.validation().errors()
            );
        }

        Instant now = clock.now();
        SellerOnboarding onboarding = new SellerOnboarding(
                principal.userSub(),
                r.normalizedProfile(),
                r.documents(),
                SellerOnboardingStatus.SELLER_PENDING,
                now,
                now
        );

        return repository.save(onboarding);
    }
}

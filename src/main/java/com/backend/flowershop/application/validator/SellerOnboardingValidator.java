package com.backend.flowershop.application.validator;

import com.backend.flowershop.application.normalize.seller.SellerProfileNormalizer;
import com.backend.flowershop.application.pipeline.RuleContext;
import com.backend.flowershop.application.pipeline.RulePipeline;
import com.backend.flowershop.domain.model.seller.SellerDocument;
import com.backend.flowershop.domain.model.seller.SellerProfile;
import com.backend.flowershop.interfaces.security.AuthPrincipal;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 作用：Seller Onboarding 校验入口协调者
 * 流程：Normalize -> Pipeline
 * 边界：不写任何 validate 逻辑（validate 必须在 rule impl）
 */
@Component
public class SellerOnboardingValidator {

    public enum Action {
        SUBMIT_ONBOARDING
    }

    private final SellerProfileNormalizer profileNormalizer;
    private final RulePipeline pipeline;

    public SellerOnboardingValidator(SellerProfileNormalizer profileNormalizer, RulePipeline pipeline) {
        this.profileNormalizer = profileNormalizer;
        this.pipeline = pipeline;
    }

    public Result validateSubmit(AuthPrincipal principal, SellerProfile profile, List<SellerDocument> documents) {
        SellerProfile normalized = profileNormalizer.normalize(profile);

        RuleContext ctx = RuleContext.forSubmitOnboarding(principal, normalized, documents);
        ValidationResult vr = pipeline.run(ctx);

        return new Result(normalized, documents == null ? List.of() : List.copyOf(documents), vr);
    }

    public record Result(
            SellerProfile normalizedProfile,
            List<SellerDocument> documents,
            ValidationResult validation
    ) {}
}

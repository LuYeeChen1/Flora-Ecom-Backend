package com.backend.flowershop.application.pipeline;

import com.backend.flowershop.domain.model.seller.SellerDocument;
import com.backend.flowershop.domain.model.seller.SellerProfile;
import com.backend.flowershop.interfaces.security.AuthPrincipal;

import java.util.List;

/**
 * 作用：规则执行上下文
 * 边界：只承载数据，不做任何 validate
 */
public record RuleContext(
        Action action,
        AuthPrincipal principal,
        SellerProfile sellerProfile,
        List<SellerDocument> sellerDocuments
) {
    public enum Action {
        SUBMIT_SELLER_ONBOARDING
    }

    public static RuleContext forSubmitOnboarding(
            AuthPrincipal principal,
            SellerProfile profile,
            List<SellerDocument> documents
    ) {
        return new RuleContext(Action.SUBMIT_SELLER_ONBOARDING, principal, profile, documents);
    }
}

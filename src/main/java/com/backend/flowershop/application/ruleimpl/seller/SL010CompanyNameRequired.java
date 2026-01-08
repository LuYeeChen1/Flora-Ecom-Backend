package com.backend.flowershop.application.ruleimpl.seller;

import com.backend.flowershop.application.pipeline.RuleContext;
import com.backend.flowershop.application.rules.seller.SellerProfileRule;
import com.backend.flowershop.application.validator.ValidationError;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 作用：公司名必填（SL010）
 */
@Component
@Order(10)
public class SL010CompanyNameRequired implements SellerProfileRule {

    @Override
    public void validate(RuleContext context, List<ValidationError> errors) {
        if (context.sellerProfile() == null) return;

        String companyName = context.sellerProfile().companyName();
        if (companyName == null || companyName.isBlank()) {
            errors.add(new ValidationError("SELLER_COMPANY_REQUIRED", "companyName", "公司名必填"));
        }
    }
}

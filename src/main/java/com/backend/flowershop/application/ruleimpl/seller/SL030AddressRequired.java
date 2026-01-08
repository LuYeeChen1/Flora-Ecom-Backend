package com.backend.flowershop.application.ruleimpl.seller;

import com.backend.flowershop.application.pipeline.RuleContext;
import com.backend.flowershop.application.rules.seller.SellerProfileRule;
import com.backend.flowershop.application.validator.ValidationError;
import com.backend.flowershop.domain.model.common.Address;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 作用：地址必填（最小）校验（SL030）
 */
@Component
@Order(30)
public class SL030AddressRequired implements SellerProfileRule {

    @Override
    public void validate(RuleContext context, List<ValidationError> errors) {
        if (context.sellerProfile() == null) return;

        Address a = context.sellerProfile().address();
        if (a == null) {
            errors.add(new ValidationError("SELLER_ADDRESS_REQUIRED", "address", "地址必填"));
            return;
        }

        if (isBlank(a.line1())) errors.add(new ValidationError("SELLER_ADDRESS_LINE1_REQUIRED", "addressLine1", "地址第1行必填"));
        if (isBlank(a.city())) errors.add(new ValidationError("SELLER_ADDRESS_CITY_REQUIRED", "city", "城市必填"));
        if (isBlank(a.country())) errors.add(new ValidationError("SELLER_ADDRESS_COUNTRY_REQUIRED", "country", "国家必填"));
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}

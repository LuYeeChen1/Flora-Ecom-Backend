package com.backend.flowershop.application.ruleimpl.seller;

import com.backend.flowershop.application.pipeline.RuleContext;
import com.backend.flowershop.application.rules.seller.SellerProfileRule;
import com.backend.flowershop.application.validator.ValidationError;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 作用：电话格式最小校验（SL020）
 */
@Component
@Order(20)
public class SL020PhoneFormat implements SellerProfileRule {

    @Override
    public void validate(RuleContext context, List<ValidationError> errors) {
        if (context.sellerProfile() == null) return;

        String phone = context.sellerProfile().phone();
        if (phone == null || phone.isBlank()) {
            errors.add(new ValidationError("SELLER_PHONE_REQUIRED", "phone", "电话必填"));
            return;
        }

        // 最小校验：只允许 + 和数字，长度 6~20
        String p = phone;
        if (!p.matches("^\\+?[0-9]{6,20}$")) {
            errors.add(new ValidationError("SELLER_PHONE_INVALID", "phone", "电话格式不正确"));
        }
    }
}

package com.backend.flowershop.application.rules.seller;

import com.backend.flowershop.application.pipeline.RuleContext;
import com.backend.flowershop.application.validator.ValidationError;

import java.util.List;

/**
 * 作用：SellerProfile 规则契约（interface only）
 */
public interface SellerProfileRule {
    void validate(RuleContext context, List<ValidationError> errors);
}

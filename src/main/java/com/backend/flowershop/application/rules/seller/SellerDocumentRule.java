package com.backend.flowershop.application.rules.seller;

import com.backend.flowershop.application.pipeline.RuleContext;
import com.backend.flowershop.application.validator.ValidationError;

import java.util.List;

/**
 * 作用：SellerDocument 规则契约（interface only）
 */
public interface SellerDocumentRule {
    void validate(RuleContext context, List<ValidationError> errors);
}

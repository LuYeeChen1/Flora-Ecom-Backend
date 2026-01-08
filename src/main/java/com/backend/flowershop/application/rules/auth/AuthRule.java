package com.backend.flowershop.application.rules.auth;

import com.backend.flowershop.application.pipeline.RuleContext;
import com.backend.flowershop.application.validator.ValidationError;

import java.util.List;

/**
 * 作用：认证相关规则契约（interface only）
 * 约束：validate 逻辑只能出现在 rule impl
 */
public interface AuthRule {
    void validate(RuleContext context, List<ValidationError> errors);
}

package com.backend.flowershop.application.rules.auth;

import com.backend.flowershop.application.pipeline.RuleContext;
import com.backend.flowershop.application.validator.ValidationError;

import java.util.List;

/**
 * 作用：role_stage 相关规则契约（interface only）
 */
public interface RoleStageRule {
    void validate(RuleContext context, List<ValidationError> errors);
}

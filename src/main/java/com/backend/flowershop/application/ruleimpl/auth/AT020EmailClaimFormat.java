package com.backend.flowershop.application.ruleimpl.auth;

import com.backend.flowershop.application.pipeline.RuleContext;
import com.backend.flowershop.application.rules.auth.AuthRule;
import com.backend.flowershop.application.validator.ValidationError;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 作用：最小 email claim 格式校验（AT020）
 * 说明：这里只做非常轻量的格式检查，避免过度验证
 */
@Component
@Order(20)
public class AT020EmailClaimFormat implements AuthRule {

    @Override
    public void validate(RuleContext context, List<ValidationError> errors) {
        if (context.principal() == null) return;

        String email = context.principal().email();
        if (email == null || email.isBlank()) return; // email 可能不是必需 claim（保持最小实现）

        // 最小检查：必须包含 @
        if (!email.contains("@")) {
            errors.add(new ValidationError("AUTH_EMAIL_INVALID", "email", "email claim 格式不正确"));
        }
    }
}

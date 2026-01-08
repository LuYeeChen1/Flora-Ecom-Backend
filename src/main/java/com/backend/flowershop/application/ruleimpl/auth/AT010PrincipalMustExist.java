package com.backend.flowershop.application.ruleimpl.auth;

import com.backend.flowershop.application.pipeline.RuleContext;
import com.backend.flowershop.application.rules.auth.AuthRule;
import com.backend.flowershop.application.validator.ValidationError;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 作用：校验 principal 必须存在（AT010）
 * 边界：只做 validate（合法性检查）
 */
@Component
@Order(10)
public class AT010PrincipalMustExist implements AuthRule {

    @Override
    public void validate(RuleContext context, List<ValidationError> errors) {
        if (context.principal() == null || context.principal().userSub() == null || context.principal().userSub().isBlank()) {
            errors.add(new ValidationError("AUTH_PRINCIPAL_MISSING", "principal", "未登录或 token 缺少 sub"));
        }
    }
}

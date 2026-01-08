package com.backend.flowershop.application.ruleimpl.auth;

import com.backend.flowershop.application.pipeline.RuleContext;
import com.backend.flowershop.application.rules.auth.RoleStageRule;
import com.backend.flowershop.application.validator.ValidationError;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 作用：校验 role_stage 是否允许执行某 action（AT030）
 * 约束：只做 validate，不写业务编排
 */
@Component
@Order(30)
public class AT030RoleStageAllowedForAction implements RoleStageRule {

    @Override
    public void validate(RuleContext context, List<ValidationError> errors) {
        if (context.action() != RuleContext.Action.SUBMIT_SELLER_ONBOARDING) return;
        if (context.principal() == null) return;

        String roleStage = context.principal().roleStage();
        if (roleStage == null || roleStage.isBlank()) {
            // 最小实现：没有 role_stage 就当 CUSTOMER（但给一个提醒级错误会阻塞流程，不符合最小闭环）
            // 所以这里不阻塞，仅保持可跑通：不加 error
            return;
        }

        // 最小允许集合：CUSTOMER / SELLER_PENDING / SELLER_REVIEWING / SELLER_REJECTED / SELLER_APPROVED
        // 禁止：SELLER_ACTIVE（已经是卖家，不应重复提交 onboarding）
        if ("SELLER_ACTIVE".equalsIgnoreCase(roleStage)) {
            errors.add(new ValidationError("ROLE_STAGE_FORBIDDEN", "custom:role_stage", "SELLER_ACTIVE 不允许提交 onboarding"));
        }
    }
}

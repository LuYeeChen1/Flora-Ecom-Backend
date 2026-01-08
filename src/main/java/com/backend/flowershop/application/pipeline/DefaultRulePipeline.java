package com.backend.flowershop.application.pipeline;

import com.backend.flowershop.application.rules.auth.AuthRule;
import com.backend.flowershop.application.rules.auth.RoleStageRule;
import com.backend.flowershop.application.rules.seller.SellerDocumentRule;
import com.backend.flowershop.application.rules.seller.SellerProfileRule;
import com.backend.flowershop.application.validator.ValidationError;
import com.backend.flowershop.application.validator.ValidationResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 作用：默认规则流水线（只编排执行，不写 validate）
 * 规则：
 * - validate 逻辑全部在 rule impl
 * - pipeline 只负责按顺序调用并汇总 errors
 */
@Component
public class DefaultRulePipeline implements RulePipeline {

    private final List<AuthRule> authRules;
    private final List<RoleStageRule> roleStageRules;
    private final List<SellerProfileRule> sellerProfileRules;
    private final List<SellerDocumentRule> sellerDocumentRules;

    public DefaultRulePipeline(
            List<AuthRule> authRules,
            List<RoleStageRule> roleStageRules,
            List<SellerProfileRule> sellerProfileRules,
            List<SellerDocumentRule> sellerDocumentRules
    ) {
        this.authRules = authRules;
        this.roleStageRules = roleStageRules;
        this.sellerProfileRules = sellerProfileRules;
        this.sellerDocumentRules = sellerDocumentRules;
    }

    @Override
    public ValidationResult run(RuleContext context) {
        List<ValidationError> errors = new ArrayList<>();

        // Auth rules
        for (AuthRule r : authRules) {
            r.validate(context, errors);
        }

        // role_stage rules
        for (RoleStageRule r : roleStageRules) {
            r.validate(context, errors);
        }

        // seller profile rules
        for (SellerProfileRule r : sellerProfileRules) {
            r.validate(context, errors);
        }

        // seller document rules
        for (SellerDocumentRule r : sellerDocumentRules) {
            r.validate(context, errors);
        }

        if (errors.isEmpty()) return ValidationResult.ok();
        return ValidationResult.fail(errors);
    }
}

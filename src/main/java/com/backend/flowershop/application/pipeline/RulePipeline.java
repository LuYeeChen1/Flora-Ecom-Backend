package com.backend.flowershop.application.pipeline;

import com.backend.flowershop.application.validator.ValidationResult;

/**
 * 作用：规则流水线接口
 * 边界：只定义契约
 */
public interface RulePipeline {
    ValidationResult run(RuleContext context);
}

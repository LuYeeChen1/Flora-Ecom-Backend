package com.backend.flowershop.application.validator;

import java.util.List;

/**
 * 作用：校验失败异常（最小实现）
 * 边界：
 * - 只承载 ValidationError 列表
 * - 由 interfaces 层统一转成标准错误 JSON

 * 说明：
 * - validate 逻辑仍然必须在 rule impl
 * - 这里仅用于“呈现层”更结构化地返回错误
 */
public class ValidationFailedException extends RuntimeException {

    private final List<ValidationError> errors;

    public ValidationFailedException(List<ValidationError> errors) {
        super("校验失败");
        this.errors = errors == null ? List.of() : List.copyOf(errors);
    }

    public List<ValidationError> errors() {
        return errors;
    }
}

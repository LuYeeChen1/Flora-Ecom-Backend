package com.backend.flowershop.application.validator;

import java.util.List;

/**
 * 作用：统一校验结果
 */
public record ValidationResult(
        boolean valid,
        List<ValidationError> errors
) {
    public static ValidationResult ok() {
        return new ValidationResult(true, List.of());
    }

    public static ValidationResult fail(List<ValidationError> errors) {
        return new ValidationResult(false, errors == null ? List.of() : List.copyOf(errors));
    }
}


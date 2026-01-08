package com.backend.flowershop.interfaces.controller.common;

import com.backend.flowershop.application.validator.ValidationError;
import com.backend.flowershop.application.validator.ValidationFailedException;
import com.backend.flowershop.domain.error.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * 作用：统一异常处理（最小实现）
 * - ValidationFailedException -> 400（结构化 field errors）
 * - DomainException -> 400
 * - 无效 token -> 401
 * - 无权限 -> 403
 * - 其他 -> 500
 *
 * 边界：只做错误响应映射，不做业务逻辑
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationFailedException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(ValidationFailedException ex) {
        ApiErrorResponse body = new ApiErrorResponse(
                "VALIDATION_FAILED",
                "请求参数不合法",
                Instant.now(),
                toFieldErrors(ex.errors())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiErrorResponse> handleDomain(DomainException ex) {
        ApiErrorResponse body = new ApiErrorResponse(
                ex.code(),
                ex.getMessage(),
                Instant.now(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidBearer(InvalidBearerTokenException ex) {
        String msg = "无效或过期的 access_token";

        // 1) 先用异常自身 message（很多时候包含 “Jwt expired at …” 等）
        if (ex.getMessage() != null && !ex.getMessage().isBlank()) {
            msg = ex.getMessage();
        }
        // 2) 再退一步用 cause 的 message（例如 JwtException / JoseException）
        else if (ex.getCause() != null && ex.getCause().getMessage() != null && !ex.getCause().getMessage().isBlank()) {
            msg = ex.getCause().getMessage();
        }

        ApiErrorResponse body = new ApiErrorResponse(
                "AUTH_TOKEN_INVALID",
                msg,
                Instant.now(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorResponse> handleDenied(AccessDeniedException ex) {
        ApiErrorResponse body = new ApiErrorResponse(
                "AUTH_FORBIDDEN",
                "无权限访问",
                Instant.now(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnknown(Exception ex) {
        ApiErrorResponse body = new ApiErrorResponse(
                "INTERNAL_ERROR",
                "服务器内部错误",
                Instant.now(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    /**
     * 作用：把 ValidationError 映射成对外 FieldError
     * 边界：纯转换
     */
    public static List<ApiErrorResponse.FieldError> toFieldErrors(List<ValidationError> errors) {
        if (errors == null || errors.isEmpty()) return List.of();

        List<ApiErrorResponse.FieldError> out = new ArrayList<>();
        for (ValidationError e : errors) {
            out.add(new ApiErrorResponse.FieldError(e.field(), e.code(), e.message()));
        }
        return out;
    }
}

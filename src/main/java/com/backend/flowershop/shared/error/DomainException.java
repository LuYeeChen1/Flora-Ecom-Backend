package com.backend.flowershop.shared.error;

/**
 * DomainException：业务异常基类（冻结文件）

 * 职责边界：
 * 1) 表示“业务层认为不可接受的状态”
 * 2) 必须携带 ErrorCode，供 API 层稳定映射 HTTP 错误
 * 3) 不关心 HTTP / Spring / JSON（纯业务语义）

 * 使用场景：
 * - Rule Pipeline 产生 violation 后，由 Application Service 转换并抛出
 * - Validator 失败时，也可以转成 DomainException（而不是 RuntimeException）

 * 输入：
 * - ErrorCode：错误类型（稳定契约）
 * - message：人类可读的错误说明（日志/前端）

 * 输出：
 * - 一个受控的 RuntimeException
 */
public class DomainException extends RuntimeException {

    private final ErrorCode errorCode;

    public DomainException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

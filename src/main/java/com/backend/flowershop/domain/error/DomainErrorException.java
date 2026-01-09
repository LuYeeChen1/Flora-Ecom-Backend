package com.backend.flowershop.domain.error;

public class DomainErrorException extends RuntimeException {
    private final String code;

    public DomainErrorException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String code() {
        return code;
    }
}

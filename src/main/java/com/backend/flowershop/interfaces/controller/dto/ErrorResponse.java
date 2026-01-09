package com.backend.flowershop.interfaces.controller.dto;

public record ErrorResponse(
        String code,
        String message
) {
}

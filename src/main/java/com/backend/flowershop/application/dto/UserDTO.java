package com.backend.flowershop.application.dto;

public record UserDTO(
        String id,
        String email,
        String username,
        String role
) {}
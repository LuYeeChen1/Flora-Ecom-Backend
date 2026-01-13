package com.backend.flowershop.application.dto;

import java.math.BigDecimal;

public record FlowerDTO(
        String id,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        String category
) {}
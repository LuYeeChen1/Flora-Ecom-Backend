package com.backend.flowershop.interfaces.controller.me.dto;

import java.util.Set;

/**
 * 作用：/me 返回 DTO
 * 边界：仅对外输出结构，不做业务逻辑
 */
public record MeResponse(
        String userSub,
        String email,
        Set<String> groups,
        String roleStage
) {}

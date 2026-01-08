package com.backend.flowershop.interfaces.security;

import java.util.Set;

/**
 * 作用：Controller 层统一使用的“当前用户信息”
 * 输入：来自 JWT claims（sub/email/groups/role_stage）
 * 输出：提供给 application/service 使用
 */
public record AuthPrincipal(
        String userSub,
        String email,
        Set<String> groups,
        String roleStage
) {}

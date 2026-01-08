package com.backend.flowershop.interfaces.security;

/**
 * 作用：集中管理 JWT claim key
 * 边界：仅常量，不做任何解析或校验
 */
public final class JwtClaimKeys {

    private JwtClaimKeys() {}

    public static final String SUB = "sub";
    public static final String EMAIL = "email";
    public static final String GROUPS = "cognito:groups";
    public static final String ROLE_STAGE = "custom:role_stage";
}

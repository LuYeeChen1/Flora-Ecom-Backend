package com.backend.flowershop.infrastructure.security;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

/**
 * 作用：JWT audience 校验（最小实现）

 * 背景：
 * - Spring Security 默认会校验 iss、签名、过期等
 * - 但不会自动校验 aud / client_id
 * - 这里强制：JWT 的 aud 必须包含你的 Cognito App Client ID

 * 边界：只做 validate，不做鉴权编排
 */
public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final String requiredAudience;

    public AudienceValidator(String requiredAudience) {
        this.requiredAudience = requiredAudience;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt jwt) {
        List<String> audiences = jwt.getAudience();
        if (audiences != null && audiences.contains(requiredAudience)) {
            return OAuth2TokenValidatorResult.success();
        }

        OAuth2Error err = new OAuth2Error(
                "invalid_token",
                "JWT aud 不匹配（client_id/audience 校验失败）",
                null
        );
        return OAuth2TokenValidatorResult.failure(err);
    }
}

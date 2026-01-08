package com.backend.flowershop.infrastructure.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

/**
 * 作用：JWT Decoder 配置（最小但完整）

 * 目标：
 * - 使用你在 properties 里提供的 issuer + jwk-set-uri（避免 discovery 超时）
 * - 强制校验：
 *   1) iss（issuer）
 *   2) exp/nbf 等标准项
 *   3) aud（必须包含 Cognito App Client ID）

 * 依赖配置（你已写在 application-dev.properties）：
 * - COGNITO_ISSUER_URI
 * - COGNITO_JWKS_URI
 * - COGNITO_CLIENT_ID
 */
@Configuration
public class JwtDecoderConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    // 你自定义的配置 key：security.jwt.client-id
    @Value("${security.jwt.client-id}")
    private String clientId;

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();

        // 1) 默认 issuer 校验 + 时间校验（exp/nbf 等）
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);

        // 2) audience 校验（你的 App Client ID）
        OAuth2TokenValidator<Jwt> withAudience = new AudienceValidator(clientId);

        // 3) 组合校验
        OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(withIssuer, withAudience);
        decoder.setJwtValidator(validator);

        return decoder;
    }
}

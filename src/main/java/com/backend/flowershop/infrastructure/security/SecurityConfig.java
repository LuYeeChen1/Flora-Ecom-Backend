package com.backend.flowershop.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 作用：最终版 Security 配置

 * 策略（冻结）：
 * - /health：匿名
 * - 其他接口：JWT 必须
 * - JWT：
 *   - iss / exp / nbf 校验
 *   - aud(client_id) 校验（由 JwtDecoderConfig + AudienceValidator）
 *   - cognito:groups -> ROLE_*
 *
 * 注意：
 * - 当前阶段不做 @PreAuthorize / group gate
 * - 后续要限制 SELLER / ADMIN，只需在 Controller 或 Method 上加注解即可
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/health").permitAll()
                        .anyRequest().authenticated()
                )

                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    /**
     * JWT -> Authentication Converter

     * 功能：
     * - 把 cognito:groups 转成 ROLE_xxx
     * - 例如：
     *   CUSTOMER -> ROLE_CUSTOMER
     *   SELLER   -> ROLE_SELLER
     *   ADMIN    -> ROLE_ADMIN
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new JwtGroupsConverter());
        return converter;
    }
}

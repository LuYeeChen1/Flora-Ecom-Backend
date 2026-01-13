package com.backend.flowershop.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 配置路径权限
                .authorizeHttpRequests(auth -> auth
                        // 🔓 允许所有访客 (Guest) 访问公开接口，无需登录
                        .requestMatchers("/api/public/**").permitAll()
                        // 🔒 其余所有 API 必须经过 Cognito 认证才能访问
                        .anyRequest().authenticated()
                )
                // 2. 启用 OAuth2 资源服务器支持（集成 AWS Cognito）
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                )
                // 3. 针对 API 场景禁用 CSRF (通常由 JWT 处理安全)
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
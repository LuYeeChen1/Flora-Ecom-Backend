package com.backend.flowershop.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. ✅ 启用 CORS (允许前端跨域访问)
                .cors(Customizer.withDefaults())

                // 2. 禁用 CSRF (REST API 不需要，且由 JWT 保证安全)
                .csrf(csrf -> csrf.disable())

                // 3. 配置路径权限 (这是修复 NPE 的关键部分)
                .authorizeHttpRequests(auth -> auth
                        // 🔓 公开接口：允许所有访客访问 (首页列表、详情)
                        .requestMatchers("/api/public/**").permitAll()
                        // 🔓 认证接口：允许访问登录/注册 (如果您的 AuthController 路径是这个)
                        .requestMatchers("/api/auth/**").permitAll()

                        // 🛡️ 卖家接口：仅限拥有 SELLER 角色的人访问
                        .requestMatchers("/api/seller/**").hasRole("SELLER")

                        // 🔥 购物车接口：必须认证 (Authenticated)
                        // 修复点：强制要求 Token，防止 Controller 拿到 null token
                        .requestMatchers("/api/cart/**").authenticated()

                        .requestMatchers("/api/orders/**").authenticated()

                        // 🔒 兜底规则：其他所有接口 (如 /api/users/me) 都必须携带 Token
                        .anyRequest().authenticated()
                )

                // 4. 启用 OAuth2 资源服务器 (解析 Cognito JWT)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                );

        return http.build();
    }

    /**
     * ✅ 核心配置：定义具体的 CORS 规则
     * 允许前端 (localhost:5173) 访问后端的所有接口
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 允许的前端地址 (Vue 默认端口)
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));

        // 允许的 HTTP 方法
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH" , "OPTIONS"));

        // 允许的 Header (关键是 Authorization，用于带 Token)
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // 是否允许携带凭证 (可选，但在某些复杂认证场景下需要)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
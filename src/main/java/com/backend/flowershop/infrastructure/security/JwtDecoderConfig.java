package com.backend.flowershop.infrastructure.security;

import org.springframework.context.annotation.Configuration;

/**
 * 作用：JWT Decoder 配置占位
 * 说明：最小实现：依赖 Spring Boot 的 issuer-uri 自动配置即可
 * 后续如需自定义 decoder/缓存策略，再在这里加 @Bean
 */
@Configuration
public class JwtDecoderConfig {}

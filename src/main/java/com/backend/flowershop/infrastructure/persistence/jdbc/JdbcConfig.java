package com.backend.flowershop.infrastructure.persistence.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * 作用：JDBC 配置（最小实现）
 * - DataSource 交给 Spring Boot 根据 spring.datasource.* 自动创建
 * - 这里只提供 JdbcTemplate
 */
@Configuration
public class JdbcConfig {

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}

package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.domain.model.User;
import com.backend.flowershop.domain.repository.UserRepository; // 1. 必须导入接口
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository; // 2. 必须加注解

import java.util.Optional;

@Repository // 3. 标记为 Bean
public class JdbcUserRepository implements UserRepository { // 4. 显式实现接口

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 核心：保存或更新用户 (Sync Logic)
    @Override // 加上 Override
    public void save(User user) {
        String sql = """
            INSERT INTO users (id, email, username, role) 
            VALUES (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE 
            email = VALUES(email), 
            username = VALUES(username),
            role = VALUES(role)
        """;
        jdbcTemplate.update(sql, user.getId(), user.getEmail(), user.getUsername(), user.getRole());
    }

    // 根据 ID 查询用户
    @Override // ✅ 加上 Override
    public Optional<User> findById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.query(sql, userRowMapper, id).stream().findFirst();
    }

    // 简单的 RowMapper
    private final RowMapper<User> userRowMapper = (rs, rowNum) -> new User(
            rs.getString("id"),
            rs.getString("email"),
            rs.getString("username"),
            rs.getString("role")
    );
}
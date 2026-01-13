package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.domain.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JdbcUserRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 核心：保存或更新用户 (Sync Logic)
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
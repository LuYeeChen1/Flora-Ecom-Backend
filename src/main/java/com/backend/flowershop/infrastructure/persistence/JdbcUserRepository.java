package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.domain.model.User;
import com.backend.flowershop.domain.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(User user) {
        // 核心更新：使用 ON DUPLICATE KEY UPDATE 实现“存在即更新，不存在即插入”
        // 且每次同步时自动刷新 last_login_at
        String sql = """
            INSERT INTO users (id, email, username, role, is_active, last_login_at) 
            VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
            ON DUPLICATE KEY UPDATE 
                email = VALUES(email), 
                username = VALUES(username),
                role = VALUES(role),
                last_login_at = CURRENT_TIMESTAMP
        """;

        jdbcTemplate.update(sql,
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getRole(),
                user.getActive()
        );
    }

    @Override
    public Optional<User> findById(String id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.query(sql, userRowMapper, id).stream().findFirst();
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> new User(
            rs.getString("id"),
            rs.getString("email"),
            rs.getString("username"),
            rs.getString("role")
    );
}
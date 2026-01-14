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
        // 确保插入/更新时包含 avatar_url
        String sql = """
            INSERT INTO users (id, email, username, avatar_url, role, is_active, last_login_at) 
            VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
            ON DUPLICATE KEY UPDATE 
                email = VALUES(email), 
                username = VALUES(username),
                avatar_url = VALUES(avatar_url),
                role = VALUES(role),
                last_login_at = CURRENT_TIMESTAMP
        """;

        jdbcTemplate.update(sql,
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getAvatarUrl(),
                user.getRole(),
                user.getActive()
        );
    }


    @Override
    public Optional<User> findById(String id) {
        String sql = "SELECT id, email, username, avatar_url, role, is_active FROM users WHERE id = ?";
        return jdbcTemplate.query(sql, userRowMapper, id).stream().findFirst();
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User(
                rs.getString("id"),
                rs.getString("email"),
                rs.getString("username"),
                rs.getString("role")
        );
        // 🔴 必须手动从 ResultSet 中提取 avatar_url 并设置到对象中
        user.setAvatarUrl(rs.getString("avatar_url"));
        user.setActive(rs.getBoolean("is_active"));
        return user;
    };
}
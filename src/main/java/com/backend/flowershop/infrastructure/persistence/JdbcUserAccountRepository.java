package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.application.port.persistence.UserAccountRepository;
import com.backend.flowershop.domain.model.UserAccount;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcUserAccountRepository implements UserAccountRepository {
    private static final String SELECT_BY_SUBJECT = "SELECT sub, email FROM users WHERE sub = ?";
    private static final String UPSERT_USER = """
            INSERT INTO users (sub, email)
            VALUES (?, ?)
            ON DUPLICATE KEY UPDATE email = VALUES(email)
            """;

    private final JdbcTemplate jdbcTemplate;

    public JdbcUserAccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<UserAccount> findBySubject(String subject) {
        List<UserAccount> results = jdbcTemplate.query(SELECT_BY_SUBJECT, new UserAccountRowMapper(), subject);
        return results.stream().findFirst();
    }

    @Override
    public void save(UserAccount account) {
        jdbcTemplate.update(UPSERT_USER, account.subject(), account.email());
    }

    private static class UserAccountRowMapper implements RowMapper<UserAccount> {
        @Override
        public UserAccount mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new UserAccount(
                    rs.getString("sub"),
                    rs.getString("email")
            );
        }
    }
}

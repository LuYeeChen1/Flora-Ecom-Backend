package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.domain.model.SellerProfile;
import com.backend.flowershop.domain.repository.SellerProfileRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JdbcSellerProfileRepository implements SellerProfileRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcSellerProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(SellerProfile profile) {
        // 核心 SQL：如果存在则更新，不存在则插入
        // 注意：每次提交都会重置状态为 'PENDING_REVIEW' (需在 Service 层或这里控制)
        // 这里我们只负责写入数据
        String sql = """
            INSERT INTO seller_profiles 
            (user_id, real_name, id_card_number, phone_number, business_address, status, applied_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            ON DUPLICATE KEY UPDATE
                real_name = VALUES(real_name),
                id_card_number = VALUES(id_card_number),
                phone_number = VALUES(phone_number),
                business_address = VALUES(business_address),
                status = VALUES(status),
                updated_at = CURRENT_TIMESTAMP
        """;

        jdbcTemplate.update(sql,
                profile.getUserId(),
                profile.getRealName(),
                profile.getIdCardNumber(),
                profile.getPhoneNumber(),
                profile.getBusinessAddress(),
                profile.getStatus()
        );
    }

    @Override
    public Optional<SellerProfile> findByUserId(String userId) {
        String sql = "SELECT * FROM seller_profiles WHERE user_id = ?";
        // 使用 Stream 防止 null
        return jdbcTemplate.query(sql, rowMapper, userId).stream().findFirst();
    }

    // 结果集映射器
    private final RowMapper<SellerProfile> rowMapper = (rs, rowNum) -> {
        SellerProfile sp = new SellerProfile();
        sp.setUserId(rs.getString("user_id"));
        sp.setRealName(rs.getString("real_name"));
        sp.setIdCardNumber(rs.getString("id_card_number"));
        sp.setPhoneNumber(rs.getString("phone_number"));
        sp.setBusinessAddress(rs.getString("business_address"));
        sp.setStatus(rs.getString("status"));
        // 如果需要时间字段，可以在 Entity 加对应字段并在这里映射
        // sp.setAppliedAt(rs.getTimestamp("applied_at").toLocalDateTime());
        return sp;
    };
}
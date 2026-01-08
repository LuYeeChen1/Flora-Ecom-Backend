package com.backend.flowershop.infrastructure.persistence.seller.mapper;

import com.backend.flowershop.domain.model.common.Address;
import com.backend.flowershop.domain.model.seller.SellerOnboarding;
import com.backend.flowershop.domain.model.seller.SellerOnboardingStatus;
import com.backend.flowershop.domain.model.seller.SellerProfile;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * 作用：seller_onboarding 表 -> Domain 映射（不含 documents）
 * 边界：只映射字段，不做业务逻辑
 */
@Component
public class SellerOnboardingRowMapper implements RowMapper<SellerOnboarding> {

    @Override
    public SellerOnboarding mapRow(ResultSet rs, int rowNum) throws SQLException {
        String userSub = rs.getString("user_sub");
        String status = rs.getString("status");

        SellerProfile profile = new SellerProfile(
                rs.getString("company_name"),
                rs.getString("phone"),
                new Address(
                        rs.getString("address_line1"),
                        rs.getString("address_line2"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("postcode"),
                        rs.getString("country")
                )
        );

        Instant createdAt = toInstant(rs.getTimestamp("created_at"));
        Instant updatedAt = toInstant(rs.getTimestamp("updated_at"));

        // documents 由 repository 额外查询并补齐
        return new SellerOnboarding(
                userSub,
                profile,
                List.of(),
                SellerOnboardingStatus.valueOf(status),
                createdAt,
                updatedAt
        );
    }

    private Instant toInstant(Timestamp ts) {
        return ts == null ? null : ts.toInstant();
    }
}

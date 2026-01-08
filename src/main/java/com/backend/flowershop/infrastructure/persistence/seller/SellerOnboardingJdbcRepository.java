package com.backend.flowershop.infrastructure.persistence.seller;

import com.backend.flowershop.application.port.out.seller.SellerOnboardingRepository;
import com.backend.flowershop.domain.model.seller.SellerDocument;
import com.backend.flowershop.domain.model.seller.SellerOnboarding;
import com.backend.flowershop.infrastructure.persistence.seller.mapper.SellerOnboardingRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 作用：SellerOnboardingRepository 的 JDBC 实现（匹配冻结类名/文件名）
 * 策略（最小实现但完整）：
 * - save(): 对 seller_onboarding 做 upsert
 * - documents：先删后插（保持最简单一致性）
 * - findByUserSub(): 查主表 + 再查 documents
 */
@Component
public class SellerOnboardingJdbcRepository implements SellerOnboardingRepository {

    private final JdbcTemplate jdbc;
    private final SellerOnboardingRowMapper onboardingRowMapper;

    public SellerOnboardingJdbcRepository(JdbcTemplate jdbc, SellerOnboardingRowMapper onboardingRowMapper) {
        this.jdbc = jdbc;
        this.onboardingRowMapper = onboardingRowMapper;
    }

    @Override
    @Transactional
    public SellerOnboarding save(SellerOnboarding onboarding) {
        // 1) upsert 主表
        upsertOnboarding(onboarding);

        // 2) documents 同步：先删后插（最小实现）
        deleteDocuments(onboarding.userSub());
        insertDocuments(onboarding.userSub(), onboarding.documents());

        // 3) 回读（拿 updated_at 等 DB 真实值）
        return findByUserSub(onboarding.userSub()).orElse(onboarding);
    }

    @Override
    public Optional<SellerOnboarding> findByUserSub(String userSub) {
        List<SellerOnboarding> list = jdbc.query(
                """
                SELECT
                  user_sub, status,
                  company_name, phone,
                  address_line1, address_line2, city, state, postcode, country,
                  created_at, updated_at
                FROM seller_onboarding
                WHERE user_sub = ?
                """,
                onboardingRowMapper,
                userSub
        );

        if (list.isEmpty()) return Optional.empty();

        SellerOnboarding base = list.get(0);
        List<SellerDocument> docs = findDocuments(userSub);

        SellerOnboarding full = new SellerOnboarding(
                base.userSub(),
                base.profile(),
                docs,
                base.status(),
                base.createdAt(),
                base.updatedAt()
        );

        return Optional.of(full);
    }

    // -------------------------
    // private helpers
    // -------------------------

    private void upsertOnboarding(SellerOnboarding o) {
        // MySQL upsert：ON DUPLICATE KEY UPDATE
        jdbc.update(
                """
                INSERT INTO seller_onboarding (
                  user_sub, status,
                  company_name, phone,
                  address_line1, address_line2, city, state, postcode, country
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                ON DUPLICATE KEY UPDATE
                  status = VALUES(status),
                  company_name = VALUES(company_name),
                  phone = VALUES(phone),
                  address_line1 = VALUES(address_line1),
                  address_line2 = VALUES(address_line2),
                  city = VALUES(city),
                  state = VALUES(state),
                  postcode = VALUES(postcode),
                  country = VALUES(country),
                  updated_at = CURRENT_TIMESTAMP
                """,
                o.userSub(),
                o.status().name(),
                o.profile().companyName(),
                o.profile().phone(),
                o.profile().address().line1(),
                o.profile().address().line2(),
                o.profile().address().city(),
                o.profile().address().state(),
                o.profile().address().postcode(),
                o.profile().address().country()
        );
    }

    private void deleteDocuments(String userSub) {
        jdbc.update("DELETE FROM seller_document WHERE user_sub = ?", userSub);
    }

    private void insertDocuments(String userSub, List<SellerDocument> docs) {
        if (docs == null || docs.isEmpty()) return;

        for (SellerDocument d : docs) {
            jdbc.update(
                    """
                    INSERT INTO seller_document (user_sub, doc_type, doc_number, doc_url)
                    VALUES (?, ?, ?, ?)
                    """,
                    userSub,
                    d.type(),
                    d.number(),
                    d.url()
            );
        }
    }

    private List<SellerDocument> findDocuments(String userSub) {
        return jdbc.query(
                """
                SELECT doc_type, doc_number, doc_url
                FROM seller_document
                WHERE user_sub = ?
                ORDER BY id ASC
                """,
                (rs, rowNum) -> new SellerDocument(
                        rs.getString("doc_type"),
                        rs.getString("doc_number"),
                        rs.getString("doc_url")
                ),
                userSub
        );
    }
}

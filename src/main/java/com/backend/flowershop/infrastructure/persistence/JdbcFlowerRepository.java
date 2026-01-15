package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.application.dto.request.FlowerDTORequest;
import com.backend.flowershop.application.dto.response.FlowerDetailDTOResponse;
import com.backend.flowershop.domain.model.Flower;
import com.backend.flowershop.domain.repository.FlowerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JdbcFlowerRepository implements FlowerRepository {

    private final JdbcTemplate jdbcTemplate;

    // 🔥 请替换为您的 S3 Bucket 区域和名称，或者配置 CloudFront
    // 格式: https://[bucket].s3.[region].amazonaws.com/
    @Value("${aws.s3.base-url:https://flower-shop-product.s3.us-east-1.amazonaws.com/}")
    private String s3BaseUrl;

    public JdbcFlowerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ... save 方法保持不变 ...
    @Override
    public void save(String sellerId, FlowerDTORequest dto) {
        String sql = """
            INSERT INTO flowers (name, description, price, stock, image_url, category, seller_id, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
        """;
        jdbcTemplate.update(sql, dto.getName(), dto.getDescription(), dto.getPrice(), dto.getStock(), dto.getImageUrl(), dto.getCategory(), sellerId);
    }

    // ... findAllPublic 方法保持不变 (但建议在 RowMapper 里拼接 s3BaseUrl) ...
    @Override
    public List<Flower> findAllPublic() {
        String sql = "SELECT id, name, description, price, stock, image_url, category, seller_id FROM flowers";
        return jdbcTemplate.query(sql, flowerRowMapper);
    }

    // 在 JdbcFlowerRepository 类中添加：
    public List<Flower> findAllBySellerId(String sellerId) {
        // 🔥 注意：这里要拼接 S3 Base URL，否则前端图片不显示
        String s3BaseUrl = "https://flower-shop-product.s3.us-east-1.amazonaws.com/";
        String sql = "SELECT id, name, description, price, stock, image_url, category, seller_id FROM flowers WHERE seller_id = ? ORDER BY created_at DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Flower flower = new Flower();
            flower.setId(rs.getLong("id"));
            flower.setName(rs.getString("name"));
            flower.setDescription(rs.getString("description"));
            flower.setPrice(rs.getBigDecimal("price"));
            flower.setStock(rs.getInt("stock"));
            // 拼接完整链接
            flower.setImageUrl(s3BaseUrl + rs.getString("image_url"));
            flower.setCategory(rs.getString("category"));
            flower.setSellerId(rs.getString("seller_id"));
            return flower;
        }, sellerId);
    }

    // ✅ 新增 2: 查询商品详情 + 卖家档案 (JOIN 查询)
    public Optional<FlowerDetailDTOResponse> findDetailById(Long flowerId) {
        String sql = """
            SELECT 
                f.id, f.name, f.description, f.price, f.stock, f.image_url, f.category,
                u.id as seller_id, u.avatar_url,
                -- 动态获取卖家名称：如果是个人取 real_name，如果是企业取 company_name
                COALESCE(i.real_name, b.company_name) as seller_name,
                -- 动态获取卖家类型
                CASE WHEN i.user_id IS NOT NULL THEN 'INDIVIDUAL' ELSE 'BUSINESS' END as seller_type,
                -- 检查状态
                COALESCE(i.status, b.status) as seller_status
            FROM flowers f
            JOIN users u ON f.seller_id = u.id
            LEFT JOIN individual_sellers i ON u.id = i.user_id
            LEFT JOIN business_sellers b ON u.id = b.user_id
            WHERE f.id = ?
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            FlowerDetailDTOResponse dto = new FlowerDetailDTOResponse();
            dto.setId(rs.getLong("id"));
            dto.setName(rs.getString("name"));
            dto.setDescription(rs.getString("description"));
            dto.setPrice(rs.getBigDecimal("price"));
            dto.setStock(rs.getInt("stock"));
            // 拼接完整 URL
            dto.setImageUrl(s3BaseUrl + rs.getString("image_url"));
            dto.setCategory(rs.getString("category"));

            dto.setSellerId(rs.getString("seller_id"));
            dto.setSellerName(rs.getString("seller_name"));
            dto.setSellerType(rs.getString("seller_type"));
            dto.setSellerAvatar(rs.getString("avatar_url"));
            // 只有 ACTIVE 状态才算 Verified
            dto.setVerified("ACTIVE".equals(rs.getString("seller_status")));

            return dto;
        }, flowerId).stream().findFirst();
    }

    // 基础 Mapper (用于列表)
    private final RowMapper<Flower> flowerRowMapper = (rs, rowNum) -> {
        Flower flower = new Flower();
        flower.setId(rs.getLong("id"));
        flower.setName(rs.getString("name"));
        flower.setDescription(rs.getString("description"));
        flower.setPrice(rs.getBigDecimal("price"));
        flower.setStock(rs.getInt("stock"));
        // 拼接完整 URL
        flower.setImageUrl(s3BaseUrl + rs.getString("image_url"));
        flower.setCategory(rs.getString("category"));
        flower.setSellerId(rs.getString("seller_id"));
        return flower;
    };
}
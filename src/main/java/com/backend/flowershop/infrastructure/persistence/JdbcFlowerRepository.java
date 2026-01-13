package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.domain.model.Flower;
import com.backend.flowershop.domain.repository.FlowerRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcFlowerRepository implements FlowerRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcFlowerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Flower> findAllPublic() {
        // 显式查询所有需要的列，包括 seller_id
        String sql = "SELECT id, name, description, price, stock, image_url, category, seller_id FROM flowers";
        return jdbcTemplate.query(sql, flowerRowMapper);
    }

    private final RowMapper<Flower> flowerRowMapper = (rs, rowNum) -> {
        Flower flower = new Flower();
        flower.setId(rs.getLong("id"));
        flower.setName(rs.getString("name"));
        flower.setDescription(rs.getString("description"));
        flower.setPrice(rs.getBigDecimal("price"));
        flower.setStock(rs.getInt("stock"));
        flower.setImageUrl(rs.getString("image_url"));
        flower.setCategory(rs.getString("category"));

        // 🔴 修复：数据库现在强制要求 seller_id，必须映射
        flower.setSellerId(rs.getString("seller_id"));

        return flower;
    };
}
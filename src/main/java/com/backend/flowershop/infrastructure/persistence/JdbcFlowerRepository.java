package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.domain.model.Flower;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class JdbcFlowerRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcFlowerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 显式 SQL 控制：获取所有公开鲜花 [cite: 32, 50]
    public List<Flower> findAllPublic() {
        String sql = "SELECT id, name, description, price, stock, image_url, category FROM flowers";
        return jdbcTemplate.query(sql, flowerRowMapper);
    }

    // 手动映射结果集到 Domain POJO [cite: 51]
    private final RowMapper<Flower> flowerRowMapper = (rs, rowNum) -> {
        Flower flower = new Flower();
        flower.setId(rs.getLong("id"));
        flower.setName(rs.getString("name"));
        flower.setDescription(rs.getString("description"));
        flower.setPrice(rs.getBigDecimal("price"));
        flower.setStock(rs.getInt("stock"));
        flower.setImageUrl(rs.getString("image_url"));
        flower.setCategory(rs.getString("category"));
        return flower;
    };
}
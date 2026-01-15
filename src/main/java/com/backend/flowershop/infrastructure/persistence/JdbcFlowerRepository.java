package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.application.dto.request.FlowerDTORequest;
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
    public void save(String sellerId, FlowerDTORequest dto) {
        String sql = """
            INSERT INTO flowers (name, description, price, stock, image_url, category, seller_id, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
        """;

        jdbcTemplate.update(sql,
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getStock(),
                dto.getImageUrl(), // 这里存的是 S3 的 Key
                dto.getCategory(),
                sellerId           // 绑定当前卖家 ID
        );
    }

    @Override
    public List<Flower> findAllPublic() {
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
        flower.setSellerId(rs.getString("seller_id"));
        return flower;
    };
}
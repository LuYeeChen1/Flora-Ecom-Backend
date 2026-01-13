package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.domain.model.Flower;
import com.backend.flowershop.domain.repository.FlowerRepository; // ✅ 必须导入接口
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository; // 必须加注解

import java.util.List;

@Repository // 1. 标记为 Bean，解决 "Could not be found"
public class JdbcFlowerRepository implements FlowerRepository { // 2. 实现接口，解决依赖注入失败

    private final JdbcTemplate jdbcTemplate;

    public JdbcFlowerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Flower> findAllPublic() {
        // SQL 查询包含所有需要的字段
        String sql = "SELECT id, name, description, price, stock, image_url, category, seller_id FROM flowers";
        return jdbcTemplate.query(sql, flowerRowMapper);
    }

    // 3. 使用 Setter 写法（修复 "Default constructor" 错误）
    private final RowMapper<Flower> flowerRowMapper = (rs, rowNum) -> {
        Flower flower = new Flower(); // 调用无参构造函数
        flower.setId(rs.getLong("id"));
        flower.setName(rs.getString("name"));
        flower.setDescription(rs.getString("description"));
        flower.setPrice(rs.getBigDecimal("price"));
        flower.setStock(rs.getInt("stock"));
        flower.setImageUrl(rs.getString("image_url"));
        flower.setCategory(rs.getString("category"));
        // 如果你的数据库有 seller_id，记得加上这行，没有就删掉
        // flower.setSellerId(rs.getString("seller_id"));
        return flower;
    };
}
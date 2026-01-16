package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.domain.model.Order;
import com.backend.flowershop.domain.model.OrderItem;
import com.backend.flowershop.domain.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value; // ✅ 引入 Value
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    // ✅ 1. 注入 S3 基础 URL (用于拼接图片链接)
    @Value("${aws.s3.base-url:https://flower-shop-product.s3.us-east-1.amazonaws.com/}")
    private String s3BaseUrl;

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long saveOrder(Order order) {
        String sql = "INSERT INTO orders (user_id, total_price, status, shipping_address) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, order.getUserId(), order.getTotalPrice(), order.getStatus(), order.getShippingAddress());
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    @Override
    public void saveOrderItems(List<OrderItem> items) {
        String sql = "INSERT INTO order_items (order_id, flower_id, flower_name, price_at_purchase, quantity) VALUES (?, ?, ?, ?, ?)";
        for (OrderItem item : items) {
            jdbcTemplate.update(sql, item.getOrderId(), item.getFlowerId(), item.getFlowerName(), item.getPriceAtPurchase(), item.getQuantity());
        }
    }

    @Override
    public List<Order> findByUserId(String userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, orderRowMapper, userId);
    }

    @Override
    public Order findById(Long orderId, String userId) {
        String sql = "SELECT * FROM orders WHERE id = ? AND user_id = ?";
        return jdbcTemplate.query(sql, orderRowMapper, orderId, userId)
                .stream().findFirst().orElse(null);
    }

    @Override
    public List<OrderItem> findOrderItemsByUserId(String userId) {
        // ✅ 2. 修改 SQL: 关联 flowers 表获取 image_url
        String sql = """
            SELECT oi.*, f.image_url 
            FROM order_items oi
            JOIN orders o ON oi.order_id = o.id
            LEFT JOIN flowers f ON oi.flower_id = f.id
            WHERE o.user_id = ?
            ORDER BY oi.order_id DESC, oi.id ASC
        """;
        return jdbcTemplate.query(sql, orderItemRowMapper, userId);
    }

    private final RowMapper<Order> orderRowMapper = (rs, rowNum) -> {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setUserId(rs.getString("user_id"));
        order.setTotalPrice(rs.getBigDecimal("total_price"));
        order.setStatus(rs.getString("status"));
        order.setShippingAddress(rs.getString("shipping_address"));
        order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return order;
    };

    private final RowMapper<OrderItem> orderItemRowMapper = (rs, rowNum) -> {
        OrderItem item = new OrderItem();
        item.setId(rs.getLong("id"));
        item.setOrderId(rs.getLong("order_id"));
        item.setFlowerId(rs.getLong("flower_id"));
        item.setFlowerName(rs.getString("flower_name"));
        item.setPriceAtPurchase(rs.getBigDecimal("price_at_purchase"));
        item.setQuantity(rs.getInt("quantity"));

        // 3. 处理图片 URL 拼接
        String rawKey = rs.getString("image_url");
        if (rawKey != null) {
            if (!rawKey.startsWith("http")) {
                item.setImageUrl(s3BaseUrl + rawKey);
            } else {
                item.setImageUrl(rawKey);
            }
        }

        return item;
    };
}
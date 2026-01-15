package com.backend.flowershop.infrastructure.persistence;

import com.backend.flowershop.domain.model.Order;
import com.backend.flowershop.domain.model.OrderItem;
import com.backend.flowershop.domain.repository.OrderRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class JdbcOrderRepository implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcOrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long saveOrder(Order order) {
        String sql = "INSERT INTO orders (user_id, total_price, status, shipping_address) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, order.getUserId());
            ps.setBigDecimal(2, order.getTotalPrice());
            ps.setString(3, order.getStatus());
            ps.setString(4, order.getShippingAddress());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public void saveOrderItems(List<OrderItem> items) {
        String sql = "INSERT INTO order_items (order_id, flower_id, flower_name, price_at_purchase, quantity) VALUES (?, ?, ?, ?, ?)";

        // 使用批量插入提高性能
        jdbcTemplate.batchUpdate(sql, items, items.size(), (ps, item) -> {
            ps.setLong(1, item.getOrderId());
            ps.setLong(2, item.getFlowerId());
            ps.setString(3, item.getFlowerName());
            ps.setBigDecimal(4, item.getPriceAtPurchase());
            ps.setInt(5, item.getQuantity());
        });
    }

    @Override
    public List<Order> findByUserId(String userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, orderRowMapper, userId);
    }

    @Override
    public Order findById(Long orderId, String userId) {
        String sql = "SELECT * FROM orders WHERE id = ? AND user_id = ?";
        Order order = jdbcTemplate.queryForObject(sql, orderRowMapper, orderId, userId);

        if (order != null) {
            // 查询该订单的所有商品项
            String itemSql = "SELECT * FROM order_items WHERE order_id = ?";
            List<OrderItem> items = jdbcTemplate.query(itemSql, itemRowMapper, orderId);
            order.setItems(items);
        }
        return order;
    }

    // RowMappers
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

    private final RowMapper<OrderItem> itemRowMapper = (rs, rowNum) -> {
        OrderItem item = new OrderItem();
        item.setId(rs.getLong("id"));
        item.setOrderId(rs.getLong("order_id"));
        item.setFlowerId(rs.getLong("flower_id"));
        item.setFlowerName(rs.getString("flower_name"));
        item.setPriceAtPurchase(rs.getBigDecimal("price_at_purchase"));
        item.setQuantity(rs.getInt("quantity"));
        return item;
    };
}
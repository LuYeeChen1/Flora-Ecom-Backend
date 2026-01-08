package com.backend.flowershop.infrastructure.persistence.jdbc;

import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 作用：提供 JDBC Connection（最小可用）
 * 边界：只提供连接，不做事务/SQL
 */
@Component
public class ConnectionProvider {

    private final DataSource dataSource;

    public ConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

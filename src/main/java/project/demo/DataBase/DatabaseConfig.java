package project.demo.DataBase;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConfig {

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load MySQL JDBC driver", e);
        }
    }

    public static Connection getConnection() {
        try {
            return ConnectionPool.getConnection();
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to get connection from pool: " + e.getMessage());
            throw new RuntimeException("Database connection failed", e);
        }
    }

    public static void shutdown() {
        ConnectionPool.shutdown();
    }
}

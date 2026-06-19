package project.demo.DataBase;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionPool {

    private static HikariDataSource dataSource;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    private static synchronized HikariDataSource getDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            return dataSource;
        }

        Properties props = new Properties();
        InputStream input = ConnectionPool.class.getResourceAsStream("/config.properties");
        if (input == null) {
            input = ConnectionPool.class.getClassLoader().getResourceAsStream("config.properties");
        }

        HikariConfig config = new HikariConfig();

        if (input != null) {
            try {
                props.load(input);
                String host = props.getProperty("db.host", "localhost");
                String port = props.getProperty("db.port", "3306");
                String dbName = props.getProperty("db.name", "handyman_db");
                String useSSL = props.getProperty("db.useSSL", "false");
                String user = props.getProperty("db.user", "root");
                String pass = props.getProperty("db.password", "");
                int maxSize = Integer.parseInt(props.getProperty("pool.maxSize", "10"));
                int minIdle = Integer.parseInt(props.getProperty("pool.minIdle", "2"));
                long connTimeout = Long.parseLong(props.getProperty("pool.connectionTimeout", "30000"));
                long idleTimeout = Long.parseLong(props.getProperty("pool.idleTimeout", "600000"));

                config.setJdbcUrl(String.format("jdbc:mysql://%s:%s/%s?useSSL=%s&allowPublicKeyRetrieval=true",
                    host, port, dbName, useSSL));
                config.setUsername(user);
                config.setPassword(pass);
                config.setMaximumPoolSize(maxSize);
                config.setMinimumIdle(minIdle);
                config.setConnectionTimeout(connTimeout);
                config.setIdleTimeout(idleTimeout);
            } catch (IOException e) {
                System.err.println("[ERROR] Failed to load config.properties: " + e.getMessage());
                fallbackConfig(config);
            } finally {
                try { input.close(); } catch (IOException ignored) {}
            }
        } else {
            System.out.println("[WARN] config.properties not found, using defaults for connection pool.");
            fallbackConfig(config);
        }

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");

        dataSource = new HikariDataSource(config);
        System.out.println("[INFO] Connection pool initialized. Max size: " + config.getMaximumPoolSize());
        return dataSource;
    }

    private static void fallbackConfig(HikariConfig config) {
        config.setJdbcUrl("jdbc:mysql://localhost:3306/handyman_db?useSSL=false&allowPublicKeyRetrieval=true");
        config.setUsername("root");
        config.setPassword("");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
    }

    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("[INFO] Connection pool shut down.");
        }
    }

    public static int getActiveConnections() {
        if (dataSource != null) {
            return dataSource.getHikariPoolMXBean().getActiveConnections();
        }
        return 0;
    }

    public static int getIdleConnections() {
        if (dataSource != null) {
            return dataSource.getHikariPoolMXBean().getIdleConnections();
        }
        return 0;
    }
}

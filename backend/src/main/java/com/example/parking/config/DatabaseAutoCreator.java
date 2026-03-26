package com.example.parking.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Hibernate 会在启动时连接到指定数据库并自动建表。
 * 如果数据库尚未创建，会导致启动失败。因此这里在容器刷新前先创建库。
 */
public class DatabaseAutoCreator implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        Environment env = applicationContext.getEnvironment();

        String url = env.getProperty("spring.datasource.url");
        String username = env.getProperty("spring.datasource.username");
        String password = env.getProperty("spring.datasource.password");
        if (url == null || username == null) return;
        if (!url.startsWith("jdbc:mysql://")) return;

        String dbName = extractDbName(url);
        if (dbName == null || dbName.isEmpty()) return;

        String serverUrl = url;
        // 去掉 /{dbName} 这一段，让连接先落到 MySQL server 默认上下文
        serverUrl = serverUrl.replace("/" + dbName, "/");
        // 如果前面已经是末尾没有参数，补一个斜杠也无妨
        if (!serverUrl.endsWith("/")) {
            // keep original - don't force
        }

        String sql = "CREATE DATABASE IF NOT EXISTS " + dbName
                + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";

        try (Connection conn = DriverManager.getConnection(serverUrl, username, password);
             Statement st = conn.createStatement()) {
            st.execute(sql);
        } catch (Exception e) {
            // 创建失败也不要完全阻断启动，让后续报出更明确的连接/权限错误
        }
    }

    private String extractDbName(String url) {
        // jdbc:mysql://host:port/dbname?param=xx
        try {
            int slash = url.indexOf('/', "jdbc:mysql://".length());
            if (slash < 0) return null;
            int q = url.indexOf('?', slash + 1);
            if (q < 0) q = url.length();
            String part = url.substring(slash + 1, q);
            return part.trim();
        } catch (Exception e) {
            return null;
        }
    }
}


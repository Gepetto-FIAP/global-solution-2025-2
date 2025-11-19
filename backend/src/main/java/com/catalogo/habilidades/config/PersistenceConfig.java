package com.catalogo.habilidades.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class PersistenceConfig {
    
    private static volatile HikariDataSource dataSource;
    
    private static synchronized void initializeDataSource() {
        if (dataSource != null) {
            return;
        }
        
        try {
            HikariConfig config = new HikariConfig();
            
            // Ler variáveis de ambiente ou usar valores padrão
            String connectString = System.getenv("ORACLE_CONNECT_STRING");
            if (connectString == null || connectString.isEmpty()) {
                connectString = "localhost:1521/XE";
            }
            
            String user = System.getenv("ORACLE_USER");
            if (user == null || user.isEmpty()) {
                user = "system";
            }
            
            String password = System.getenv("ORACLE_PASSWORD");
            if (password == null || password.isEmpty()) {
                password = "password";
            }
            
            // Configurar HikariCP
            config.setJdbcUrl("jdbc:oracle:thin:@//" + connectString);
            config.setUsername(user);
            config.setPassword(password);
            config.setDriverClassName("oracle.jdbc.OracleDriver");
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(5);
            config.setConnectionTimeout(30000);
            config.setIdleTimeout(600000);
            config.setMaxLifetime(1800000);
            
            dataSource = new HikariDataSource(config);
            
            // Testar conexão
            System.out.println("✓ DataSource inicializado com sucesso");
            System.out.println("  JDBC URL: " + config.getJdbcUrl());
            System.out.println("  User: " + config.getUsername());
        } catch (Exception e) {
            System.err.println("✗ Erro ao inicializar DataSource:");
            System.err.println("  Mensagem: " + e.getMessage());
            System.err.println("  ORACLE_CONNECT_STRING: " + System.getenv("ORACLE_CONNECT_STRING"));
            System.err.println("  ORACLE_USER: " + System.getenv("ORACLE_USER"));
            e.printStackTrace();
            throw new RuntimeException("Erro ao inicializar DataSource: " + e.getMessage(), e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            initializeDataSource();
        }
        return dataSource.getConnection();
    }
    
    public static void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}


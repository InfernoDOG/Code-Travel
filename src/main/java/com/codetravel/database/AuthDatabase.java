package com.codetravel.database;

import java.io.File;
import java.sql.*;
import java.util.Optional;

public class AuthDatabase {
    // Указываем путь к базе внутри папки database
    private static final String DB_URL = "jdbc:sqlite:database/reg-log.db";
    private static AuthDatabase instance;
    
    private AuthDatabase() {
        System.out.println("AuthDatabase: Инициализация файловой базы пользователей");
        
        // Автоматически создаем папку database в корне, если её нет
        File dbFolder = new File("database");
        if (!dbFolder.exists()) {
            dbFolder.mkdirs();
            System.out.println("AuthDatabase: Создана папка 'database/'");
        }
        
        initializeDatabase();
    }
    
    public static synchronized AuthDatabase getInstance() {
        if (instance == null) {
            instance = new AuthDatabase();
        }
        return instance;
    }
    
    // Метод принудительной регистрации драйвера для защиты от сбоев JavaFX
    private void registerDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Критическая ошибка: Драйвер SQLite не найден! " + e.getMessage());
        }
    }
    
    public void initializeDatabase() {
        registerDriver(); // Регистрируем драйвер перед подключением

        String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT UNIQUE NOT NULL,"
                + "email TEXT UNIQUE,"
                + "password_hash TEXT NOT NULL,"
                + "salt TEXT NOT NULL,"
                + "created_at DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "last_login DATETIME,"
                + "remember_token TEXT"
                + ");";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(createTableSQL);
            System.out.println("AuthDatabase: База данных успешно проверена и готова к работе.");
            
        } catch (SQLException e) {
            System.err.println("AuthDatabase: Ошибка инициализации БД - " + e.getMessage());
        }
    }
    
    public boolean validateLogin(String username, String password) {
        return authenticateUser(username, password).isPresent();
    }

    public boolean registerUser(String username, String email, String password) {
        registerDriver();
        String salt = generateSalt();
        String passwordHash = hashPassword(password, salt);
        
        String sql = "INSERT INTO users (username, email, password_hash, salt) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, passwordHash);
            pstmt.setString(4, salt);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("AuthDatabase: Ошибка регистрации - " + e.getMessage());
            return false;
        }
    }
    
    public Optional<User> authenticateUser(String username, String password) {
        registerDriver();
        String sql = "SELECT id, username, email, password_hash, salt FROM users WHERE username = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    String salt = rs.getString("salt");
                    String computedHash = hashPassword(password, salt);
                    
                    if (storedHash.equals(computedHash)) {
                        updateLastLogin(rs.getInt("id"));
                        
                        User user = new User();
                        user.setId(rs.getInt("id"));
                        user.setUsername(rs.getString("username"));
                        user.setEmail(rs.getString("email"));
                        
                        return Optional.of(user);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("AuthDatabase: Ошибка аутентификации - " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    public boolean saveRememberToken(int userId, String token) {
        registerDriver();
        String sql = "UPDATE users SET remember_token = ? WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, token);
            pstmt.setInt(2, userId);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("AuthDatabase: Ошибка сохранения токена - " + e.getMessage());
            return false;
        }
    }
    
    public Optional<User> getUserByRememberToken(String token) {
        registerDriver();
        String sql = "SELECT id, username, email FROM users WHERE remember_token = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, token);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    
                    return Optional.of(user);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("AuthDatabase: Ошибка поиска по токену - " + e.getMessage());
        }
        
        return Optional.empty();
    }
    
    private void updateLastLogin(int userId) {
        registerDriver();
        String sql = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("AuthDatabase: Ошибка обновления времени входа - " + e.getMessage());
        }
    }
    
    private String generateSalt() {
        return java.util.UUID.randomUUID().toString();
    }
    
    private String hashPassword(String password, String salt) {
        try {
            return java.util.Base64.getEncoder().encodeToString(
                java.security.MessageDigest.getInstance("SHA-256")
                    .digest((password + salt).getBytes())
            );
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
    
    public static class User {
        private int id;
        private String username;
        private String email;
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}
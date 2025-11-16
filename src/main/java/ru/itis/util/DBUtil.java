package ru.itis.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:postgresql://localhost:5432/bd_semestr?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC"; // Кодировка на всякий случай
    private static final String USER = "postgres";
    private static final String PASSWORD = "0044";
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found.", e);
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

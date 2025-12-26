package com.library.app.util;

import java.sql.*;
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static Connection connection = null;

    public static Connection getConnection() {
        
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Database is good.");
            } catch (SQLException e) {
            throw new RuntimeException("Database connection failed: " + e.getMessage());
        }
        }
        return connection;
    }
}
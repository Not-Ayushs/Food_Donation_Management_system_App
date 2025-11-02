package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/food_donor";
    private static final String USER = "root";
    private static final String PASSWORD = "Ayush12031";

    /**
     * Establishes and returns a new connection for each call.
     * This is the correct pattern to avoid "Connection Closed" errors.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
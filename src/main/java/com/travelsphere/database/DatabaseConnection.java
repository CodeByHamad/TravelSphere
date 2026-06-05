package com.travelsphere.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // MySQL standard connection configurations for XAMPP
    private static final String URL = "jdbc:mysql://localhost:3306/travelsphere_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Default XAMPP password is empty ""
    
    private static Connection connection = null;

    /**
     * Establishes or returns the active connection to your local MySQL database.
     * @return Connection object, or null if connection fails.
     */
    public static Connection getConnection() {
        try {
            // If connection is closed or hasn't been made yet, establish a new one
            if (connection == null || connection.isClosed()) {
                // Force load the MySQL JDBC driver class from your pom.xml dependency
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("[Database] Connected to MySQL (travelsphere_db) successfully.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("[Database] MySQL JDBC Driver missing! Check your pom.xml setup: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("[Database] Connection failed! Make sure MySQL is running in XAMPP: " + e.getMessage());
        }
        return connection;
    }
}
package com.travelsphere.controllers;

import com.travelsphere.database.DatabaseConnection;
import com.travelsphere.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthController {
    private final Connection conn;
    private static User currentUser = null; // Holds the active session user

    public AuthController() {
        // Automatically grabs the active database connection
        this.conn = DatabaseConnection.getConnection();
    }

    // --- MODULE 1 FEATURE: SIGN UP ---
    public boolean registerUser(String name, String email, String password) {
        String sql = "INSERT INTO users(name, email, password) VALUES(?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
            return true; // Successfully registered
        } catch (SQLException e) {
            System.err.println("[Auth] Registration failed: " + e.getMessage());
            return false; // Will fail if email already exists (UNIQUE constraint)
        }
    }

    // --- MODULE 1 FEATURE: LOGIN ---
    public boolean loginUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Track user session globally
                currentUser = new User(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password")
                );
                return true; // Credentials matched
            }
        } catch (SQLException e) {
            System.err.println("[Auth] Login error: " + e.getMessage());
        }
        return false; // Authentication failed
    }

    // --- SESSION MANAGEMENT ---
    public static User getCurrentUser() {
        return currentUser;
    }

    public static void logout() {
        currentUser = null;
    }
}
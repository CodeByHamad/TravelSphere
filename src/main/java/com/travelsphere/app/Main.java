package com.travelsphere.app;

import com.travelsphere.gui.LoginFrame;

public class Main {
    public static void main(String[] args) {
        // Launch the application UI safely on Java's rendering thread
        java.awt.EventQueue.invokeLater(() -> {
            // 🌟 FORCE THE LOGIN FRAME TO BE THE FIRST WINDOW
            LoginFrame login = new LoginFrame();
            login.setLocationRelativeTo(null); // Center it beautifully on the screen
            login.setVisible(true);
        });
    }
}
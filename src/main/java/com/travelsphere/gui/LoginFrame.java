/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.travelsphere.gui;

/**
 *
 * @author Ahsan
 */
public class LoginFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LoginFrame.class.getName());

    
    public LoginFrame() {
        initComponents();
        try {
            java.net.URL imgURL = getClass().getResource("/com/travelsphere/assets/icons/DashboardBackground.png");
            if (imgURL != null) {
                this.rawBackgroundImage = new javax.swing.ImageIcon(imgURL).getImage();
                System.out.println("[GUI] Background asset cached successfully.");
            } else {
                System.err.println("[GUI Error] Could not locate Background1.png in target resources.");
            }
        } catch (Exception e) {
            System.err.println("Error loading background image asset: " + e.getMessage());
        }
        this.setSize(1000, 700);
        this.setMinimumSize(new java.awt.Dimension(800, 650));
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(null);

        // 2. HARDWARE ACCELERATED BACKGROUND LAYER PANEL
        backgroundPanel = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                if (rawBackgroundImage != null) {
                    java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                    // BILINEAR rendering delegates scaling to GPU interpolation for instant processing
                    g2.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2.drawImage(rawBackgroundImage, 0, 0, getWidth(), getHeight(), this);
                    g2.dispose();
                }
            }
        };
        backgroundPanel.setLayout(null);
        this.getContentPane().add(backgroundPanel);
        this.getContentPane().setComponentZOrder(backgroundPanel, 0); 
        
        // --- 3. PREMIUM GLASSMORPHISM PANEL CONTAINER ---
        javax.swing.JPanel glassCard = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Translucent crisp white surface fill (Opacity 165/255)
                g2.setColor(new java.awt.Color(100, 120, 140, 120)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
                
                // Refined container border outline
                g2.setColor(new java.awt.Color(100, 120, 140, 120));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 35, 35);
                g2.dispose();
            }
        };
        glassCard.setOpaque(false);
        glassCard.setLayout(null);
        this.getLayeredPane().add(glassCard, javax.swing.JLayeredPane.PALETTE_LAYER);

        // --- 4. HEADER INTERFACE ELEMENTS ---
        javax.swing.JLabel titleLabel = new javax.swing.JLabel("TravelSphere", javax.swing.SwingConstants.CENTER);
        titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 44));
        titleLabel.setForeground(new java.awt.Color(255, 255, 255));
        glassCard.add(titleLabel);

        javax.swing.JLabel sloganLabel = new javax.swing.JLabel("Explore the World, One Sphere at a Time", javax.swing.SwingConstants.CENTER);
        sloganLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        sloganLabel.setForeground(new java.awt.Color(255, 255, 255));
        glassCard.add(sloganLabel);

        // --- 5. INPUT FORM FIELDS WITH INTERNAL TEXT PADDING ---
        javax.swing.JLabel emailLabel = new javax.swing.JLabel("Email Address:");
        emailLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        emailLabel.setForeground(new java.awt.Color(255, 255, 255));
        glassCard.add(emailLabel);

        // Safely cache image references to prevent I/O lag during text typing repaints
        final java.awt.Image[] emailImgHolder = new java.awt.Image[1];
        final java.awt.Image[] lockImgHolder = new java.awt.Image[1];
        final java.awt.Image[] eyeIconsImg = new java.awt.Image[2]; 

        try {
            java.net.URL emailURL = getClass().getResource("/com/travelsphere/assets/icons/email.png");
            java.net.URL lockURL = getClass().getResource("/com/travelsphere/assets/icons/lock.png");
            java.net.URL closedURL = getClass().getResource("/com/travelsphere/assets/icons/eye_closed.png");
            java.net.URL openURL = getClass().getResource("/com/travelsphere/assets/icons/eye_open.png");
            
            if (emailURL != null) emailImgHolder[0] = new javax.swing.ImageIcon(emailURL).getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH);
            if (lockURL != null) lockImgHolder[0] = new javax.swing.ImageIcon(lockURL).getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH);
            if (closedURL != null) eyeIconsImg[0] = new javax.swing.ImageIcon(closedURL).getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
            if (openURL != null) eyeIconsImg[1] = new javax.swing.ImageIcon(openURL).getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        } catch(Exception e) { 
            System.out.println("Asset pipeline loading dropped details."); 
        }
        emailField = new javax.swing.JTextField() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && emailImgHolder[0] != null) {
                    g.drawImage(emailImgHolder[0], 12, (getHeight() - 18) / 2, this);
                }
            }
        };
        emailField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        emailField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 210, 225), 1),
            javax.swing.BorderFactory.createEmptyBorder(0, 42, 0, 12)
        ));
        glassCard.add(emailField);

        javax.swing.JLabel passwordLabel = new javax.swing.JLabel("Password:");
        passwordLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        passwordLabel.setForeground(new java.awt.Color(255, 255, 255));
        glassCard.add(passwordLabel);
        final boolean[] isPasswordVisible = { false };
        passwordField = new javax.swing.JPasswordField() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                if (getPassword().length == 0 && lockImgHolder[0] != null) {
                    g.drawImage(lockImgHolder[0], 12, (getHeight() - 18) / 2, this);
                }
                java.awt.Image currentEye = isPasswordVisible[0] ? eyeIconsImg[1] : eyeIconsImg[0];
                if (currentEye != null) {
                    g.drawImage(currentEye, getWidth() - 32, (getHeight() - 20) / 2, this);
                }
            }
        };
        passwordField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        passwordField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 210, 225), 1),
            javax.swing.BorderFactory.createEmptyBorder(0, 42, 0, 42)
        ));
        glassCard.add(passwordField);
        passwordField.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getX() >= (passwordField.getWidth() - 40)) {
                    if (isPasswordVisible[0]) {
                        passwordField.setEchoChar('•');
                    } else {
                        passwordField.setEchoChar((char) 0);
                    }
                    isPasswordVisible[0] = !isPasswordVisible[0];
                    passwordField.repaint();
                }
            }
        });
        passwordField.addMouseMotionListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                if (e.getX() >= (passwordField.getWidth() - 40)) {
                    passwordField.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
                } else {
                    passwordField.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.TEXT_CURSOR));
                }
            }
        });
        javax.swing.JLabel forgotPwdLabel = new javax.swing.JLabel("Forgot Password?", javax.swing.SwingConstants.RIGHT);
        forgotPwdLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        forgotPwdLabel.setForeground(new java.awt.Color(100, 180, 255));
        forgotPwdLabel.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        
        forgotPwdLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                forgotPwdLabel.setForeground(new java.awt.Color(0, 51, 153));
                forgotPwdLabel.setText("<html><u>Forgot Password?</u></html>");
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                forgotPwdLabel.setForeground(new java.awt.Color(100, 180, 255));
                forgotPwdLabel.setText("Forgot Password?");
            }
        });
        glassCard.add(forgotPwdLabel);
        
        // --- 6. ACTION BUTTONS WITH LIVE MYSQL VALIDATION ENGINE ---
        javax.swing.JButton loginButton = new javax.swing.JButton("Log In");
        loginButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        loginButton.setBackground(new java.awt.Color(0, 82, 204));
        loginButton.setForeground(java.awt.Color.WHITE);
        loginButton.setFocusPainted(false);
        try {
            java.net.URL loginURL = getClass().getResource("/com/travelsphere/assets/icons/login.png");
            if (loginURL != null) {
                java.awt.Image logIconScaled = new javax.swing.ImageIcon(loginURL).getImage().getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
                loginButton.setIcon(new javax.swing.ImageIcon(logIconScaled));
                loginButton.setIconTextGap(8);
            }
        } catch(Exception e) {}
        
        // BINDING AUTHENTICATION VERIFIER EVENT LINK TO YOUR DATABASE
        // BINDING AUTHENTICATION VERIFIER EVENT LINK TO YOUR DATABASE
        loginButton.addActionListener((java.awt.event.ActionEvent e) -> {
            String inputEmail = emailField.getText().trim();
            String inputPass = new String(passwordField.getPassword()).trim();
            
            // Quick client side sanity checking
            if (inputEmail.isEmpty() || inputPass.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(LoginFrame.this, 
                        "Please fill out both your Email and Password.", "Missing Credentials", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Query matches email AND password against column names in your schema
            String authSQL = "SELECT * FROM users WHERE email = ? AND password = ?";
            
            try (java.sql.Connection conn = com.travelsphere.database.DatabaseConnection.getConnection()) {
                if (conn == null) {
                    javax.swing.JOptionPane.showMessageDialog(LoginFrame.this,
                            "Database offline! Ensure XAMPP MySQL control engine is active.", "System Connection Failure", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try (java.sql.PreparedStatement pstmt = conn.prepareStatement(authSQL)) {
                    pstmt.setString(1, inputEmail);
                    pstmt.setString(2, inputPass);
                    
                    try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            // Match found! Capture information dynamically if needed
                            String userName = rs.getString("username");
                            
                            // STEP 1: Extract the unique user_id row number for this specific user
                            int userId = rs.getInt("user_id"); 
                            
                            javax.swing.JOptionPane.showMessageDialog(LoginFrame.this, 
                                    "Login Successful! Welcome back, " + userName + ".", "Access Granted", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                            
                            LoginFrame.this.dispose();
                            
                            java.awt.EventQueue.invokeLater(() -> {
                
                                DashboardFram dashboard = new DashboardFram(userId);
                                
                                dashboard.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH); 

                                dashboard.setLocationRelativeTo(null); 
                                dashboard.setVisible(true);
                            });
                            
                        } else {
                            javax.swing.JOptionPane.showMessageDialog(LoginFrame.this, 
                                    "Invalid Email or Password. Please check entry parameters.", "Authentication Denied", javax.swing.JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            } catch (java.sql.SQLException ex) {
                javax.swing.JOptionPane.showMessageDialog(LoginFrame.this,
                        "Database processing failure: " + ex.getMessage(), "Query Fault", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });
        glassCard.add(loginButton);

        javax.swing.JButton signUpButton = new javax.swing.JButton("Sign Up");
        signUpButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        signUpButton.setBackground(java.awt.Color.WHITE);
        signUpButton.setForeground(new java.awt.Color(11, 29, 58));
        signUpButton.setFocusPainted(false);
        signUpButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 210, 225), 1));
        try {
            java.net.URL signupURL = getClass().getResource("/com/travelsphere/assets/icons/signup.png");
            if (signupURL != null) {
                java.awt.Image signIconScaled = new javax.swing.ImageIcon(signupURL).getImage().getScaledInstance(16, 16, java.awt.Image.SCALE_SMOOTH);
                signUpButton.setIcon(new javax.swing.ImageIcon(signIconScaled));
                signUpButton.setIconTextGap(8);
            }
        } catch(Exception e) {}
        glassCard.add(signUpButton);
        signUpButton.addActionListener((java.awt.event.ActionEvent evt) -> {
            java.awt.Window currentWindow = javax.swing.SwingUtilities.getWindowAncestor(signUpButton);
            int currentExtendedState = javax.swing.JFrame.NORMAL;
            
            if (currentWindow instanceof javax.swing.JFrame jFrame) {
                currentExtendedState = jFrame.getExtendedState();
            }
            
            SignUpFrame signUp = new SignUpFrame();
            signUp.setExtendedState(currentExtendedState);
            signUp.setVisible(true);
            
            LoginFrame.this.setVisible(false);
        });
        javax.swing.JLabel dividerLabel = new javax.swing.JLabel("————— or —————", javax.swing.SwingConstants.CENTER);
        dividerLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        dividerLabel.setForeground(new java.awt.Color(255, 255, 255));
        glassCard.add(dividerLabel);

        javax.swing.border.Border normalBorder = javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2);
        javax.swing.border.Border hoverBorder = javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 82, 204, 100), 1, true),
            javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1)
        );

        javax.swing.JButton googleIcon = new javax.swing.JButton();
        googleIcon.setContentAreaFilled(false);
        googleIcon.setBorder(normalBorder);
        googleIcon.setFocusPainted(false);
        googleIcon.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        try {
            java.net.URL gURL = getClass().getResource("/com/travelsphere/assets/icons/google.png");
            if (gURL != null) {
                googleIcon.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(gURL).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) {}
        googleIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { googleIcon.setBorder(hoverBorder); }
            @Override public void mouseExited(java.awt.event.MouseEvent e) { googleIcon.setBorder(normalBorder); }
        });

        javax.swing.JButton fbIcon = new javax.swing.JButton();
        fbIcon.setContentAreaFilled(false);
        fbIcon.setBorder(normalBorder);
        fbIcon.setFocusPainted(false);
        fbIcon.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        try {
            java.net.URL fbURL = getClass().getResource("/com/travelsphere/assets/icons/facebook.png");
            if (fbURL != null) {
                fbIcon.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(fbURL).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) {}
        fbIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { fbIcon.setBorder(hoverBorder); }
            @Override public void mouseExited(java.awt.event.MouseEvent e) { fbIcon.setBorder(normalBorder); }
        });

        javax.swing.JButton appleIcon = new javax.swing.JButton();
        appleIcon.setContentAreaFilled(false);
        appleIcon.setBorder(normalBorder);
        appleIcon.setFocusPainted(false);
        appleIcon.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        try {
            java.net.URL aURL = getClass().getResource("/com/travelsphere/assets/icons/apple.png");
            if (aURL != null) {
                appleIcon.setIcon(new javax.swing.ImageIcon(new javax.swing.ImageIcon(aURL).getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
            }
        } catch (Exception e) {}
        appleIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { appleIcon.setBorder(hoverBorder); }
            @Override public void mouseExited(java.awt.event.MouseEvent e) { appleIcon.setBorder(normalBorder); }
        });

        glassCard.add(googleIcon);
        glassCard.add(fbIcon);
        glassCard.add(appleIcon);
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int width = getContentPane().getWidth();
                int height = getContentPane().getHeight();

                if (width <= 0 || height <= 0) return;
                backgroundPanel.setBounds(0, 0, width, height);
                int cardWidth = 440;
                int cardHeight = 580;
                int cardX = (width - cardWidth) / 2;
                int cardY = (height - cardHeight) / 2;
                glassCard.setBounds(cardX, cardY, cardWidth, cardHeight);

                int formItemWidth = 340;
                int itemX = (cardWidth - formItemWidth) / 2;

                titleLabel.setBounds(itemX, 35, formItemWidth, 50);
                sloganLabel.setBounds(itemX - 20, 85, formItemWidth + 40, 20);

                emailLabel.setBounds(itemX, 130, formItemWidth, 25);
                emailField.setBounds(itemX, 155, formItemWidth, 40);

                passwordLabel.setBounds(itemX, 210, formItemWidth, 25);
                passwordField.setBounds(itemX, 235, formItemWidth, 40); 
                
                forgotPwdLabel.setBounds(itemX, 280, formItemWidth, 20);

                loginButton.setBounds(itemX, 315, formItemWidth, 42);
                signUpButton.setBounds(itemX, 370, formItemWidth, 42);

                dividerLabel.setBounds(itemX, 430, formItemWidth, 20);

                int iconSize = 40;
                int gap = 20;
                int totalRowWidth = (3 * iconSize) + (2 * gap);
                int startRowX = (cardWidth - totalRowWidth) / 2;
                int iconY = 465;

                googleIcon.setBounds(startRowX, iconY, iconSize, iconSize);
                fbIcon.setBounds(startRowX + iconSize + gap, iconY, iconSize, iconSize);
                appleIcon.setBounds(startRowX + 2 * (iconSize + gap), iconY, iconSize, iconSize);
                
                getContentPane().repaint();
            }
        });
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor*/
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
   
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private java.awt.Image rawBackgroundImage = null;
    private javax.swing.JPanel backgroundPanel;
    private javax.swing.JTextField emailField;
    private javax.swing.JPasswordField passwordField;
}
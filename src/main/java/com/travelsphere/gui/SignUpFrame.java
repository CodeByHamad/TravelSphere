/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.travelsphere.gui;

/**
 *
 * @author Ahsan
 */
public class SignUpFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SignUpFrame.class.getName());
    private javax.swing.JPanel backgroundPanel;
    private java.awt.Image rawBackgroundImage = null;
    
    private javax.swing.JTextField nameField;
    private javax.swing.JTextField emailField;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JCheckBox termsCheckBox;
    private javax.swing.JToggleButton eyeToggleButton;
    
    public SignUpFrame() {
        initComponents();
        initCustomComponents(); 
    }
    private void initCustomComponents() {
        try {
            java.net.URL imgURL = getClass().getResource("/com/travelsphere/assets/icons/DashboardBackground.png");
            if (imgURL != null) {
                this.rawBackgroundImage = new javax.swing.ImageIcon(imgURL).getImage();
            } else {
                logger.log(java.util.logging.Level.WARNING, "Background image path not found!");
            }
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error loading background image asset", e);
        }

        // 2. LOAD & SCALE EMBEDDED FIELD ICONS 
        java.awt.Image emailIcon = null;
        java.awt.Image lockIcon = null;
        java.awt.Image eyeOpenImg = null;
        java.awt.Image eyeClosedImg = null;
        try {
    java.net.URL emailURL = getClass().getResource("/com/travelsphere/assets/icons/email.png");
    java.net.URL lockURL = getClass().getResource("/com/travelsphere/assets/icons/lock.png");
    java.net.URL openURL = getClass().getResource("/com/travelsphere/assets/icons/eye_open.png");
    java.net.URL closedURL = getClass().getResource("/com/travelsphere/assets/icons/eye_closed.png");
    
    System.out.println("Email URL: " + emailURL);
    System.out.println("Lock URL: " + lockURL);
    System.out.println("Eye Open URL: " + openURL);
    System.out.println("Eye Closed URL: " + closedURL);
    // -----------------------------
            if (emailURL != null) emailIcon = new javax.swing.ImageIcon(emailURL).getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH);
            if (lockURL != null) lockIcon = new javax.swing.ImageIcon(lockURL).getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH);
            if (openURL != null) eyeOpenImg = new javax.swing.ImageIcon(openURL).getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH);
            if (closedURL != null) eyeClosedImg = new javax.swing.ImageIcon(closedURL).getImage().getScaledInstance(18, 18, java.awt.Image.SCALE_SMOOTH);
        } catch (Exception e) {
            logger.log(java.util.logging.Level.WARNING, "Failed to load form input field icons", e);
        }
        final java.awt.Image finalEmailIcon = emailIcon;
        final java.awt.Image finalLockIcon = lockIcon;
        final javax.swing.ImageIcon eyeOpenIcon = (eyeOpenImg != null) ? new javax.swing.ImageIcon(eyeOpenImg) : null;
        final javax.swing.ImageIcon eyeClosedIcon = (eyeClosedImg != null) ? new javax.swing.ImageIcon(eyeClosedImg) : null;

        this.setSize(1000, 700);
        this.setMinimumSize(new java.awt.Dimension(800, 650));
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(null);
        backgroundPanel = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                if (rawBackgroundImage != null) {
                    java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                    g2.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2.drawImage(rawBackgroundImage, 0, 0, getWidth(), getHeight(), this);
                    g2.dispose();
                }
            }
        };
        backgroundPanel.setLayout(null);
        this.getContentPane().add(backgroundPanel);
        this.getContentPane().setComponentZOrder(backgroundPanel, 0); 
        
        // --- 1. GLASSMORPHISM PANEL CONTAINER ---
        javax.swing.JPanel glassCard = new javax.swing.JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new java.awt.Color(100, 120, 140, 120)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
                g2.setColor(new java.awt.Color(100, 120, 140, 120));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 35, 35);
                g2.dispose();
            }
        };
        glassCard.setOpaque(false);
        glassCard.setLayout(null);
        this.getLayeredPane().add(glassCard, javax.swing.JLayeredPane.PALETTE_LAYER);

        // --- 2. HEADER INTERFACE ELEMENTS ---
        javax.swing.JLabel titleLabel = new javax.swing.JLabel("Create Account", javax.swing.SwingConstants.CENTER);
        titleLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 36));
        titleLabel.setForeground(new java.awt.Color(255, 255, 255));
        glassCard.add(titleLabel);

        javax.swing.JLabel sloganLabel = new javax.swing.JLabel("Join TravelSphere and explore the world.", javax.swing.SwingConstants.CENTER);
        sloganLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        sloganLabel.setForeground(new java.awt.Color(255, 255, 255));
        glassCard.add(sloganLabel);

        // --- 3. INPUT FORM FIELDS ---
        javax.swing.JLabel nameLabel = new javax.swing.JLabel("Full Name:");
        nameLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        nameLabel.setForeground(new java.awt.Color(255, 255, 255));
        glassCard.add(nameLabel);

        nameField = new javax.swing.JTextField();
        nameField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        nameField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 210, 225), 1),
            javax.swing.BorderFactory.createEmptyBorder(0, 12, 0, 12)
        ));
        glassCard.add(nameField);

        javax.swing.JLabel emailLabel = new javax.swing.JLabel("Email Address:");
        emailLabel.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        emailLabel.setForeground(new java.awt.Color(255, 255, 255));
        glassCard.add(emailLabel);

        // DYNAMIC HIDING ICON EMAIL FIELD
        emailField = new javax.swing.JTextField() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                if (finalEmailIcon != null && getText().isEmpty()) {
                    java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                    g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                    int y = (getHeight() - 18) / 2; 
                    g2.drawImage(finalEmailIcon, 12, y, this); 
                    g2.dispose();
                }
            }
        };
        emailField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { emailField.repaint(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { emailField.repaint(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { emailField.repaint(); }
        });
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

        // DYNAMIC HIDING ICON PASSWORD FIELD
        passwordField = new javax.swing.JPasswordField() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                if (finalLockIcon != null && getPassword().length == 0) {
                    java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                    g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                    int y = (getHeight() - 18) / 2;
                    g2.drawImage(finalLockIcon, 12, y, this);
                    g2.dispose();
                }
            }
        };
        passwordField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { passwordField.repaint(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { passwordField.repaint(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { passwordField.repaint(); }
        });
        passwordField.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        passwordField.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 210, 225), 1),
            javax.swing.BorderFactory.createEmptyBorder(0, 42, 0, 40) // End gap for button bounds
        ));
        glassCard.add(passwordField);

        // IN-BOX EYE TOGGLE BUTTON
        eyeToggleButton = new javax.swing.JToggleButton();
        if (eyeClosedIcon != null) eyeToggleButton.setIcon(eyeClosedIcon);
        eyeToggleButton.setBorderPainted(false);
        eyeToggleButton.setContentAreaFilled(false);
        eyeToggleButton.setFocusPainted(false);
        eyeToggleButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        eyeToggleButton.addActionListener(e -> {
            if (eyeToggleButton.isSelected()) {
                if (eyeOpenIcon != null) eyeToggleButton.setIcon(eyeOpenIcon);
                passwordField.setEchoChar((char) 0);
            } else {
                if (eyeClosedIcon != null) eyeToggleButton.setIcon(eyeClosedIcon);
                passwordField.setEchoChar('•');
            }
        });
        glassCard.add(eyeToggleButton);

        termsCheckBox = new javax.swing.JCheckBox("I agree to the Terms of Service & Privacy Policy");
        termsCheckBox.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
        termsCheckBox.setForeground(new java.awt.Color(255, 255, 255));
        termsCheckBox.setOpaque(false);
        glassCard.add(termsCheckBox);

        // --- 4. ACTION BUTTONS ---
        javax.swing.JButton signUpButton = new javax.swing.JButton("Register Now");
        signUpButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        signUpButton.setBackground(new java.awt.Color(0, 82, 204));
        signUpButton.setForeground(java.awt.Color.WHITE);
        signUpButton.addActionListener((java.awt.event.ActionEvent e) -> {
            String inputUser = nameField.getText().trim();
            String inputEmail = emailField.getText().trim();
            String inputPass = new String(passwordField.getPassword()).trim();
            
            // Validation Rule 1: Empty Fields Check
            if (inputUser.isEmpty() || inputEmail.isEmpty() || inputPass.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(SignUpFrame.this, 
                        "All fields must be completely filled out!", "Validation Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Validation Rule 2: Email Structural Integrity Check
            if (!inputEmail.contains("@") || !inputEmail.contains(".")) {
                javax.swing.JOptionPane.showMessageDialog(SignUpFrame.this, 
                        "Please provide a valid email format (e.g., explorer@travelsphere.com).", "Invalid Input", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Validation Rule 3: Pass Length Security Bound
            if (inputPass.length() < 6) {
                javax.swing.JOptionPane.showMessageDialog(SignUpFrame.this, 
                        "Password is insecure! It must be at least 6 characters.", "Security Rule", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Validation Rule 4: Legal Consent Checklist
            if (!termsCheckBox.isSelected()) {
                javax.swing.JOptionPane.showMessageDialog(SignUpFrame.this, 
                        "You must agree to the Terms of Service to create a session.", "Consent Required", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Database Writing Phase
            String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            try (java.sql.Connection conn = com.travelsphere.database.DatabaseConnection.getConnection()) {
                if (conn == null) {
                    javax.swing.JOptionPane.showMessageDialog(SignUpFrame.this,
                            "Database Engine Offline! Check XAMPP panel state.", "Database Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try (java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, inputUser);
                    pstmt.setString(2, inputEmail);
                    pstmt.setString(3, inputPass);
                    
                    int rowCount = pstmt.executeUpdate();
                    if (rowCount > 0) {
                        javax.swing.JOptionPane.showMessageDialog(SignUpFrame.this, 
                                "Account registered successfully! Welcome to TravelSphere.", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        
                        // Navigate to the Login screen immediately
                        SignUpFrame.this.dispose();
                        new LoginFrame().setVisible(true);
                    }
                }
            } catch (java.sql.SQLException ex) {
                // Gracefully intercept unique database column constraint errors
                if (ex.getMessage().contains("Duplicate entry")) { 
                    javax.swing.JOptionPane.showMessageDialog(SignUpFrame.this,
                            "This account name or email is already taken!", "Registration Conflict", javax.swing.JOptionPane.ERROR_MESSAGE);
                } else {
                    javax.swing.JOptionPane.showMessageDialog(SignUpFrame.this, 
                            "MySQL Query System Failure: " + ex.getMessage(), "SQL Database Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        glassCard.add(signUpButton);

        javax.swing.JButton cancelButton = new javax.swing.JButton("Back to Login");
        cancelButton.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        cancelButton.setBackground(java.awt.Color.WHITE);
        cancelButton.setForeground(new java.awt.Color(11, 29, 58));
        cancelButton.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(200, 210, 225), 1));
        glassCard.add(cancelButton);

        // Frame Shift Navigation Logic
        cancelButton.addActionListener((var e) -> {
            java.awt.Window currentWindow = javax.swing.SwingUtilities.getWindowAncestor(cancelButton);
            int currentExtendedState = javax.swing.JFrame.NORMAL;
            
            if (currentWindow instanceof javax.swing.JFrame) {
                currentExtendedState = ((javax.swing.JFrame) currentWindow).getExtendedState();
            }
            
            LoginFrame login = new LoginFrame();
            login.setExtendedState(currentExtendedState);
            login.setVisible(true);
            
            this.dispose();
        });

        // Optimization Layout Engine Listener
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int width = getContentPane().getWidth();
                int height = getContentPane().getHeight();

                if (width <= 0 || height <= 0) return;

                // Adjust bounds with zero re-scaling calculation delay
                backgroundPanel.setBounds(0, 0, width, height);

                int cardWidth = 440;
                int cardHeight = 580;
                glassCard.setBounds((width - cardWidth) / 2, (height - cardHeight) / 2, cardWidth, cardHeight);

                int formItemWidth = 340;
                int itemX = (cardWidth - formItemWidth) / 2;

                titleLabel.setBounds(itemX, 30, formItemWidth, 45);
                sloganLabel.setBounds(itemX - 20, 75, formItemWidth + 40, 20);
                nameLabel.setBounds(itemX, 115, formItemWidth, 20);
                nameField.setBounds(itemX, 135, formItemWidth, 40);
                emailLabel.setBounds(itemX, 185, formItemWidth, 20);
                emailField.setBounds(itemX, 205, formItemWidth, 40);
                passwordLabel.setBounds(itemX, 255, formItemWidth, 20);
                passwordField.setBounds(itemX, 275, formItemWidth, 40);
                
                // Pin the eye toggle explicitly inside the password field frame boundaries
                eyeToggleButton.setBounds(itemX + formItemWidth - 35, 283, 26, 24);
                
                termsCheckBox.setBounds(itemX, 325, formItemWidth, 25);
                signUpButton.setBounds(itemX, 375, formItemWidth, 42);
                cancelButton.setBounds(itemX, 430, formItemWidth, 42);
                
                getContentPane().repaint();
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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

        java.awt.EventQueue.invokeLater(() -> new SignUpFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
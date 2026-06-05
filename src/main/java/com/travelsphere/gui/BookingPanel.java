/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.travelsphere.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BookingPanel extends JPanel {
    private final DashboardFram dashboardFrame;
    private final JPanel centerContentDeck;
    private final CardLayout contentDeckLayout;
    
    private String itemTitle;
    private final boolean isHotel;
    private final String previousPanelKey; // Track exactly where the user came from
    private double baseRate = 0.0;
    private String description = "";

    // UI Components
    private JTextField travelersCountField;
    private JTextField stayLengthDaysField;
    private JTextField targetDateField;
    private JLabel totalInvoiceLabel;

    public BookingPanel(DashboardFram dashboardFrame, JPanel centerContentDeck, CardLayout contentDeckLayout, String itemTitle, boolean isHotel, String previousPanelKey) {
        this.dashboardFrame = dashboardFrame;
        this.centerContentDeck = centerContentDeck;
        this.contentDeckLayout = contentDeckLayout;
        this.previousPanelKey = previousPanelKey; 
        this.isHotel = isHotel; 
        
        this.itemTitle = itemTitle;
        if (!isHotel && itemTitle.contains(",")) {
            this.itemTitle = itemTitle.split(",")[0].trim();
        }

        setOpaque(false);
        setLayout(new GridBagLayout()); 
        
        fetchDatabaseDetails();
        initUI();
    }

    private void fetchDatabaseDetails() {
        String query = isHotel 
            ? "SELECT amenities AS desc_text, price_per_night AS base_rate FROM hotels WHERE hotel_name LIKE ? LIMIT 1"
            : "SELECT description AS desc_text, price_per_person AS base_rate FROM destinations WHERE city LIKE ? LIMIT 1";
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/travelsphere_db", "root", "");
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, "%" + itemTitle.trim() + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    this.description = rs.getString("desc_text");
                    this.baseRate = rs.getDouble("base_rate");
                } else {
                    this.baseRate = isHotel ? 149.00 : 299.00;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error loading data: " + ex.getMessage());
            this.baseRate = isHotel ? 149.00 : 299.00; 
        }
    }

    private void initUI() {
        JPanel formCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 41, 59, 190)); 
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        formCard.setOpaque(false);
        formCard.setPreferredSize(new Dimension(540, 550));
        formCard.setMinimumSize(new Dimension(540, 550)); 
        formCard.setLayout(null); 

        JLabel mainHeader = new JLabel(itemTitle, SwingConstants.CENTER);
        mainHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        mainHeader.setForeground(Color.WHITE);
        mainHeader.setBounds(20, 15, 500, 35);
        formCard.add(mainHeader);

        JLabel typeBadge = new JLabel(" Category: " + (isHotel ? "Hotel Stay" : "Tour Destination"));
        typeBadge.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        typeBadge.setForeground(new Color(180, 195, 220));
        typeBadge.setBounds(35, 50, 250, 20);
        formCard.add(typeBadge);

        JTextArea textBodyArea = new JTextArea(description == null || description.isEmpty() ? "No additional details available." : description);
        textBodyArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textBodyArea.setLineWrap(true);
        textBodyArea.setWrapStyleWord(true);
        textBodyArea.setEditable(false);
        textBodyArea.setForeground(new Color(220, 225, 230));
        
        // 🌟 STYLING CHANGE: Strip opaque features off the text box to reveal the glass panel background
        textBodyArea.setOpaque(false);
        textBodyArea.setBackground(new Color(0, 0, 0, 0));
        
        JScrollPane descScroll = new JScrollPane(textBodyArea);
        descScroll.setBounds(35, 80, 470, 100);
        
        // 🌟 STYLING CHANGE: Make the JScrollPane translucent and set a soft UI border layout match
        descScroll.setOpaque(false);
        descScroll.getViewport().setOpaque(false);
        descScroll.setBackground(new Color(0, 0, 0, 0));
        descScroll.getViewport().setBackground(new Color(0, 0, 0, 0));
        
        descScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 45), 1, true), 
                "Package Highlights", 0, 0, new Font("Segoe UI", Font.BOLD, 14), Color.WHITE));
        formCard.add(descScroll);

        String unitLabelString = isHotel ? "Rate per Night: $" : "Rate per Person: $";
        JLabel costBreakdownLabel = new JLabel(unitLabelString + String.format("%.2f", baseRate));
        costBreakdownLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        costBreakdownLabel.setForeground(new Color(0, 195, 255));
        costBreakdownLabel.setBounds(35, 195, 470, 25);
        formCard.add(costBreakdownLabel);

        JLabel peoplePrompt = new JLabel("Number of People:");
        peoplePrompt.setFont(new Font("Segoe UI", Font.BOLD, 12));
        peoplePrompt.setForeground(Color.WHITE);
        peoplePrompt.setBounds(35, 235, 150, 20);
        formCard.add(peoplePrompt);

        travelersCountField = new JTextField("1");
        travelersCountField.setFont(new Font("Segoe UI", Font.BOLD, 14));
        travelersCountField.setHorizontalAlignment(JTextField.CENTER);
        travelersCountField.setBounds(35, 260, 220, 35);
        formCard.add(travelersCountField);

        JLabel stayDurationPrompt = new JLabel(isHotel ? "Duration of Stay (Nights):" : "Duration of Tour (Days):");
        stayDurationPrompt.setFont(new Font("Segoe UI", Font.BOLD, 12));
        stayDurationPrompt.setForeground(Color.WHITE);
        stayDurationPrompt.setBounds(285, 235, 220, 20);
        formCard.add(stayDurationPrompt);

        stayLengthDaysField = new JTextField("1");
        stayLengthDaysField.setFont(new Font("Segoe UI", Font.BOLD, 14));
        stayLengthDaysField.setHorizontalAlignment(JTextField.CENTER);
        stayLengthDaysField.setBounds(285, 260, 220, 35);
        formCard.add(stayLengthDaysField);

        JLabel datePrompt = new JLabel(" Target Booking Date (YYYY-MM-DD):");
        datePrompt.setFont(new Font("Segoe UI", Font.BOLD, 12));
        datePrompt.setForeground(Color.WHITE);
        datePrompt.setBounds(35, 310, 300, 20);
        formCard.add(datePrompt);

        String currentTodayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        targetDateField = new JTextField(currentTodayDate);
        targetDateField.setFont(new Font("Segoe UI", Font.BOLD, 14));
        targetDateField.setHorizontalAlignment(JTextField.CENTER);
        targetDateField.setBounds(35, 335, 470, 35);
        formCard.add(targetDateField);

        totalInvoiceLabel = new JLabel(String.format("Calculated Bill Checkout Amount: $%.2f", baseRate));
        totalInvoiceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalInvoiceLabel.setForeground(new Color(60, 255, 160)); 
        totalInvoiceLabel.setBounds(35, 400, 470, 30);
        formCard.add(totalInvoiceLabel);

        javax.swing.event.DocumentListener livePriceEngine = new javax.swing.event.DocumentListener() {
            private void compute() {
                SwingUtilities.invokeLater(() -> {
                    try {
                        String headsText = travelersCountField.getText().trim();
                        String daysText = stayLengthDaysField.getText().trim();
                        if (headsText.isEmpty() || daysText.isEmpty()) return;
                        
                        int heads = Integer.parseInt(headsText);
                        int days = Integer.parseInt(daysText);
                        if (heads > 0 && days > 0) {
                            totalInvoiceLabel.setText(String.format("Calculated Bill Checkout Amount: $%.2f", (baseRate * heads * days)));
                        }
                    } catch (NumberFormatException nfe) {
                        totalInvoiceLabel.setText("Calculated Bill Checkout Amount: $0.00");
                    }
                });
            }
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { compute(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { compute(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { compute(); }
        };
        travelersCountField.getDocument().addDocumentListener(livePriceEngine);
        stayLengthDaysField.getDocument().addDocumentListener(livePriceEngine);

        // 🛠️ FIX: Direct Navigation Back To ItemDetailPanel
        JButton returnBtn = new JButton("<-Return");
        returnBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        returnBtn.setBounds(35, 455, 130, 40);
        returnBtn.setBackground(new Color(71, 85, 105));
        returnBtn.setForeground(Color.WHITE);
        returnBtn.setFocusPainted(false);
        returnBtn.addActionListener(e -> {
            boolean panelFlipped = false;

            // Step 1: Scan for your ItemDetailPanel explicitly in memory to bypass CardLayout string bugs
            for (Component comp : centerContentDeck.getComponents()) {
                String compClass = comp.getClass().getSimpleName();
                if (compClass.contains("ItemDetail") || "itemDetailView".equals(comp.getName())) {
                    comp.setVisible(true);
                    panelFlipped = true;
                    break;
                }
            }

            // Step 2: Fallback explicitly to the CardLayout registry name strings if scanning was blocked
            if (panelFlipped) {
                this.setVisible(false); 
            } else {
                try {
                    contentDeckLayout.show(centerContentDeck, "itemDetail");
                } catch (Exception ex1) {
                    try {
                        contentDeckLayout.show(centerContentDeck, "itemDetailView");
                    } catch (Exception ex2) {
                        // If everything else fails, routing via your fallback registration works!
                        contentDeckLayout.show(centerContentDeck, previousPanelKey);
                    }
                }
            }

            centerContentDeck.revalidate();
            centerContentDeck.repaint();
        });
        formCard.add(returnBtn); 

        JButton secureCheckoutBtn = new JButton("Secure Transaction Booking");
        secureCheckoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        secureCheckoutBtn.setBounds(185, 455, 320, 40);
        secureCheckoutBtn.setBackground(new Color(30, 180, 110));
        secureCheckoutBtn.setForeground(Color.WHITE);
        secureCheckoutBtn.setFocusPainted(false);
        secureCheckoutBtn.addActionListener(e -> processCheckoutPayment());
        formCard.add(secureCheckoutBtn);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        
        add(formCard, gbc);
    }

    private void processCheckoutPayment() {
        try {
            int validatedPeopleCount = Integer.parseInt(travelersCountField.getText().trim());
            int validatedDaysCount = Integer.parseInt(stayLengthDaysField.getText().trim());
            String userEnteredDate = targetDateField.getText().trim();
            
            if (validatedPeopleCount <= 0 || validatedDaysCount <= 0) throw new NumberFormatException();
            if (userEnteredDate.length() != 10) {
                JOptionPane.showMessageDialog(this, "Please enter date format matching YYYY-MM-DD.", "Date Entry Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double finalTransactionTotal = baseRate * validatedPeopleCount * validatedDaysCount;
            int activeUserId = dashboardFrame.getLoggedInUserId();
            String customerName = "User Account"; 

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/travelsphere_db", "root", "")) {
                
                String fetchUserSQL = "SELECT username FROM users WHERE user_id = ? LIMIT 1";
                try (PreparedStatement uPstmt = conn.prepareStatement(fetchUserSQL)) {
                    uPstmt.setInt(1, activeUserId);
                    try (ResultSet rs = uPstmt.executeQuery()) {
                        if (rs.next()) {
                            customerName = rs.getString("username");
                        }
                    }
                }

                String persistBookingSQL = "INSERT INTO bookings (user_id, destination, booking_type, booking_date, price) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(persistBookingSQL)) {
                    pstmt.setInt(1, activeUserId); 
                    pstmt.setString(2, itemTitle); 
                    pstmt.setString(3, isHotel ? "Hotel Stay" : "Tour Destination"); 
                    pstmt.setString(4, userEnteredDate); 
                    pstmt.setDouble(5, finalTransactionTotal);
                    pstmt.executeUpdate();
                }

                String textReceiptOutput = String.format("""
                    ========================================
                         TRAVELSPHERE SYSTEM RECEIPT       
                    ========================================
                    👤 Account Username: %s
                    🆔 User Reference ID: #%d
                    📍 Reserved Package Name: %s
                    🗂️ Booking Segment Profile: %s
                    📅 Targeted Departure Date: %s
                    👥 Checked Headcount Volume: %d Guest(s)
                    ⌛ Term Duration Period: %d Day/Night(s)
                    💰 Standard Baseline Unit Cost: $%.2f
                    ----------------------------------------
                    🚀 ACCOUNT SETTLED TRANSACTION AMOUNT: $%.2f
                    
                    Status Code: Confirmed / Processed Securely
                    ========================================""",
                    customerName, activeUserId, itemTitle, (isHotel ? "Hotel Stay" : "Tour Destination"), userEnteredDate, validatedPeopleCount, validatedDaysCount, baseRate, finalTransactionTotal
                );
                
                JOptionPane.showMessageDialog(this, textReceiptOutput, "Reservation Invoice Logged", JOptionPane.INFORMATION_MESSAGE);
                
                // Route to item details view frame
                try {
                    contentDeckLayout.show(centerContentDeck, "itemDetail");
                } catch(Exception eKey) {
                    contentDeckLayout.show(centerContentDeck, previousPanelKey);
                }

            } catch (SQLException sqlEx) {
                JOptionPane.showMessageDialog(this, "SQL Error: " + sqlEx.getMessage(), "Database Rejection", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Validation Failure: Please review inputs.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
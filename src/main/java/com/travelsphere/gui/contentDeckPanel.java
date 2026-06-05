package com.travelsphere.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.*;

public class contentDeckPanel extends JPanel {

    // --- Database Configuration Constants ---
    private static final String DB_URL = "jdbc:mysql://localhost:3306/travelsphere_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "your_password"; // ⚠️ Update to your MySQL password

    // --- Global Context Bridge Variable ---
    private String activeSearchLocation = ""; 

    // --- UI Layout & Component References ---
    private final CardLayout contentDeckLayout;
    private final JPanel centerContentDeck;
    
    // View Panels
    private JPanel dashboardViewPanel;
    private JPanel exploreViewPanel;
    private JPanel hotelsViewPanel;
    private JPanel bookingsViewPanel;
    private JPanel guidesViewPanel;
    private JPanel galleryViewPanel;
    private JPanel profileViewPanel;

    // Grid Containers
    private JPanel dashboardDestPanel;
    private JPanel dashboardHotelsPanel;
    private JPanel exploreGrid;
    private JPanel hotelsGrid;

    // Active Input Elements
    private JTextField searchExploreField;
    private JTextField searchHotelsField;
    private JScrollPane exploreScrollPane;
    private JScrollPane hotelsScrollPane;

    // Sidebar Menu Button References
    private JButton dashboardBtn, bookingsBtn, exploreBtn, hotelsBtn, guidesBtn, gallaryBtn, settingsBtn;
    
    // Widgets
    private JPanel travelTrendsWidget;
    private JPanel recentSearchesWidget;

    public contentDeckPanel() {
        setOpaque(false);
        setLayout(null); // Retains your exact custom fluid rendering bounds matrix
        
        contentDeckLayout = new CardLayout();
        centerContentDeck = new JPanel(contentDeckLayout);
        centerContentDeck.setOpaque(false);
        
        // Initialize Core View Modules
        initDashboardViewComponent();
        initBookingsViewComponent();
        initExploreViewComponent();
        inithotelsViewPanel();
        initGuidesViewComponent();
        initGalleryViewComponent();
        initProfileViewComponent();
        
        // Register Cards into Layout Deck Container
        centerContentDeck.add(dashboardViewPanel, "DASHBOARD");
        centerContentDeck.add(bookingsViewPanel, "BOOKINGS");
        centerContentDeck.add(exploreViewPanel, "EXPLORE");
        centerContentDeck.add(hotelsViewPanel, "HOTELS");
        centerContentDeck.add(guidesViewPanel, "GUIDES");
        centerContentDeck.add(galleryViewPanel, "GALLERY");
        centerContentDeck.add(profileViewPanel, "PROFILE");
        
        add(centerContentDeck);
        
        // Fire initial hydration for the landing Dashboard context
        loadDashboardPreviews();
    }

    // =================================================================
    // CORE ROUTING & DATABASE ENGINE
    // =================================================================

    private void switchActiveScreen(JButton clickedButton, String cardLookUpKey) {
        JButton[] menuButtons = {dashboardBtn, bookingsBtn, exploreBtn, hotelsBtn, guidesBtn, gallaryBtn, settingsBtn};
        for (JButton btn : menuButtons) {
            if (btn != null) {
                btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
                btn.setName("");
            }
        }
        applyActiveMenuState(clickedButton);
        
        // Context-driven live database execution sequences
        switch (cardLookUpKey) {
            case "DASHBOARD" -> loadDashboardPreviews();
            case "EXPLORE" -> {
                loadAllDestinations(""); // Load all rows fresh on click
                if (searchExploreField != null) searchExploreField.setText("Search destination hot spots...");
            }
            case "HOTELS" -> loadHotels(); // Auto-evaluates if a contextual search constraint exists!
        }
        
        boolean isDashboard = cardLookUpKey.equals("DASHBOARD");
        if (travelTrendsWidget != null) travelTrendsWidget.setVisible(isDashboard);
        if (recentSearchesWidget != null) recentSearchesWidget.setVisible(isDashboard);
        
        contentDeckLayout.show(centerContentDeck, cardLookUpKey);
        
        if (this.getComponentListeners().length > 0) {
            ComponentListener cl = this.getComponentListeners()[0];
            cl.componentResized(new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED));
        }
    }

    private void loadDashboardPreviews() {
        if (dashboardDestPanel == null || dashboardHotelsPanel == null) return;
        dashboardDestPanel.removeAll();
        dashboardHotelsPanel.removeAll();
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            // Pull a preview slice of destinations
            String destSql = "SELECT name, description, rating, image_path FROM destinations LIMIT 3";
            try (PreparedStatement stmt = conn.prepareStatement(destSql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dashboardDestPanel.add(createDestinationCard(rs.getString("name"), rs.getString("description"), rs.getFloat("rating"), rs.getString("image_path")));
                }
            }
            // Pull a preview slice of hotels
            String hotelSql = "SELECT name, details, rating, image_path FROM hotels LIMIT 3";
            try (PreparedStatement stmt = conn.prepareStatement(hotelSql); ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dashboardHotelsPanel.add(createDestinationCard(rs.getString("name"), rs.getString("details"), rs.getFloat("rating"), rs.getString("image_path")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Dashboard database loading exception thrown: " + e.getMessage());
        }
        dashboardDestPanel.revalidate(); dashboardDestPanel.repaint();
        dashboardHotelsPanel.revalidate(); dashboardHotelsPanel.repaint();
    }

    private void loadAllDestinations(String searchQuery) {
        if (exploreGrid == null) return;
        exploreGrid.removeAll();
        this.activeSearchLocation = searchQuery.trim(); // Cache location context globally

        String sql = "SELECT name, description, rating, image_path FROM destinations";
        boolean filtering = !activeSearchLocation.isEmpty();
        if (filtering) {
            sql += " WHERE name LIKE ? OR description LIKE ?";
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (filtering) {
                stmt.setString(1, "%" + activeSearchLocation + "%");
                stmt.setString(2, "%" + activeSearchLocation + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    exploreGrid.add(createDestinationCard(rs.getString("name"), rs.getString("description"), rs.getFloat("rating"), rs.getString("image_path")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        exploreGrid.revalidate(); exploreGrid.repaint();
    }

    private void loadHotels() {
        if (hotelsGrid == null) return;
        hotelsGrid.removeAll();
        
        String sql = "SELECT name, details, rating, image_path FROM hotels";
        boolean hasContext = !activeSearchLocation.isEmpty();
        if (hasContext) {
            sql += " WHERE details LIKE ? OR name LIKE ?";
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (hasContext) {
                stmt.setString(1, "%" + activeSearchLocation + "%");
                stmt.setString(2, "%" + activeSearchLocation + "%");
                if (searchHotelsField != null) {
                    searchHotelsField.setText("Suggested stays near: " + activeSearchLocation);
                }
            } else {
                if (searchHotelsField != null) {
                    searchHotelsField.setText("Search luxury hotels, resorts, and villas...");
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    hotelsGrid.add(createDestinationCard(rs.getString("name"), rs.getString("details"), rs.getFloat("rating"), rs.getString("image_path")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        hotelsGrid.revalidate(); hotelsGrid.repaint();
    }

    // =================================================================
    // UI COMPONENT VIEWS INITIALIZATION
    // =================================================================

    private void initDashboardViewComponent() {
        dashboardViewPanel = createBlankGlassViewScreen("Explorer Analytics Board");
        
        dashboardDestPanel = new JPanel(null); dashboardDestPanel.setOpaque(false);
        dashboardHotelsPanel = new JPanel(null); dashboardHotelsPanel.setOpaque(false);
        
        JLabel lbl1 = new JLabel("Trending Hotspots"); lbl1.setForeground(Color.WHITE); lbl1.setBounds(20,70,200,20);
        JLabel lbl2 = new JLabel("Featured Stays"); lbl2.setForeground(Color.WHITE); lbl2.setBounds(20,260,200,20);
        
        dashboardViewPanel.add(lbl1);
        dashboardViewPanel.add(lbl2);
    }

    private void initExploreViewComponent() {
        exploreViewPanel = createBlankGlassViewScreen("Explore Global Destinations");
        
        searchExploreField = new JTextField("Search destination hot spots...");
        setupGlassTextFieldStyle(searchExploreField);
        searchExploreField.setBounds(20, 70, 500, 35);
        
        // Dynamic relational search activation listener
        searchExploreField.addActionListener(e -> {
            loadAllDestinations(searchExploreField.getText());
        });
        
        exploreGrid = new JPanel(null); exploreGrid.setOpaque(false);
        exploreScrollPane = new JScrollPane(exploreGrid);
        exploreScrollPane.setOpaque(false); exploreScrollPane.getViewport().setOpaque(false);
        exploreScrollPane.setBorder(null);
        exploreScrollPane.setBounds(20, 120, 550, 350);
        
        exploreViewPanel.add(searchExploreField);
        exploreViewPanel.add(exploreScrollPane);
    }

    private void inithotelsViewPanel() {
        hotelsViewPanel = createBlankGlassViewScreen("Premium Accommodations & Stays");
        
        JLabel noticeLabel = new JLabel("Explore luxury stays, boutique villas, and premium resorts worldwide.");
        noticeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        noticeLabel.setForeground(new Color(200, 200, 200));
        noticeLabel.setBounds(20, 65, 500, 25);
        hotelsViewPanel.add(noticeLabel);

        searchHotelsField = new JTextField("Search luxury hotels, resorts, and villas...");
        setupGlassTextFieldStyle(searchHotelsField);
        searchHotelsField.setBounds(20, 100, 500, 35);
        
        // Manual override search field tracking link
        searchHotelsField.addActionListener(e -> {
            activeSearchLocation = searchHotelsField.getText();
            loadHotels();
        });
        
        hotelsGrid = new JPanel(null); hotelsGrid.setOpaque(false);
        hotelsScrollPane = new JScrollPane(hotelsGrid);
        hotelsScrollPane.setOpaque(false); hotelsScrollPane.getViewport().setOpaque(false);
        hotelsScrollPane.setBorder(null);
        
        hotelsViewPanel.add(searchHotelsField);
        hotelsViewPanel.add(hotelsScrollPane);
    }

    private void initBookingsViewComponent() {
        bookingsViewPanel = createBlankGlassViewScreen("Your Itinerary Ledger");
        
        JLabel activeHeader = new JLabel("📅 Current & Upcoming Reservations");
        activeHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        activeHeader.setForeground(new Color(0, 200, 255));
        activeHeader.setBounds(20, 75, 400, 25);
        bookingsViewPanel.add(activeHeader);
        
        JPanel activeTableHeader = createTableRowLayout("Destination / Hotel", "Booking Reference", "Status", true);
        activeTableHeader.setBounds(20, 110, 600, 35);
        bookingsViewPanel.add(activeTableHeader);
        
        JPanel row1 = createTableRowLayout("Amalfi, Italy • Monastero Santa Rosa", "TS-FL-90822", "CONFIRMED", false);
        row1.setBounds(20, 150, 600, 45);
        bookingsViewPanel.add(row1);
        
        JPanel row2 = createTableRowLayout("Kyoto, Japan • Hoshinoya Ryokan", "TS-HT-11029", "PAID", false);
        row2.setBounds(20, 200, 600, 45);
        bookingsViewPanel.add(row2);
        
        JLabel historyHeader = new JLabel("⏳ Archival Travel History");
        historyHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        historyHeader.setForeground(new Color(180, 180, 180));
        historyHeader.setBounds(20, 270, 400, 25);
        bookingsViewPanel.add(historyHeader);
        
        JPanel historyTableHeader = createTableRowLayout("Past Destination Location", "Date Visited", "Journey Type", true);
        historyTableHeader.setBounds(20, 305, 600, 35);
        bookingsViewPanel.add(historyTableHeader);
        
        JPanel hRow1 = createTableRowLayout("Paris, France • Eiffel Suites", "March 14, 2025", "Leisure Vacation", false);
        hRow1.setBounds(20, 345, 600, 45);
        bookingsViewPanel.add(hRow1);
        
        JPanel hRow2 = createTableRowLayout("London, UK • The Savoy Hotel", "October 09, 2024", "Business Flight", false);
        hRow2.setBounds(20, 395, 600, 45);
        bookingsViewPanel.add(hRow2);
    }

    private void initGuidesViewComponent() {
        guidesViewPanel = createBlankGlassViewScreen("Curated Travel Knowledge Guides");
        JPanel guidesContainer = new JPanel(null); guidesContainer.setOpaque(false);
        guidesContainer.setBounds(20, 80, 550, 400);
        
        JPanel g1 = createListIndicatorCard("📚 Ultimate Packing List for Mediterranean Cruises", "Read Time: 5 mins • Updated recently"); g1.setBounds(0,0,530,60);
        JPanel g2 = createListIndicatorCard("⛩️ Navigating Kyoto's Subway System: A Local's Guide", "Read Time: 8 mins • Includes map attachments"); g2.setBounds(0,70,530,60);
        JPanel g3 = createListIndicatorCard("💳 Traveling Smart: Avoiding International Card Fees", "Read Time: 4 mins • Financial safety rules"); g3.setBounds(0,140,530,60);
        
        guidesContainer.add(g1); guidesContainer.add(g2); guidesContainer.add(g3);
        guidesViewPanel.add(guidesContainer);
    }

    private void initGalleryViewComponent() {
        galleryViewPanel = createBlankGlassViewScreen("Your Tour Memories Shared Album");
        JButton uploadBtn = new JButton("📷 Upload New Memory Image");
        uploadBtn.setBounds(20, 70, 240, 35);
        uploadBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        galleryViewPanel.add(uploadBtn);
    }

    private void initProfileViewComponent() {
        profileViewPanel = createBlankGlassViewScreen("Your Explorer Account Profile");
        
        int contentWidth = 600, panelWidth = 800;
        int midX = (panelWidth - contentWidth) / 2;
        
        JPanel avatarCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 200, 255)); g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(40, 50, 70)); g2.fillOval(4, 4, getWidth() - 8, getHeight() - 8);
                g2.dispose();
            }
        };
        avatarCircle.setLayout(null);
        avatarCircle.setBounds(midX + (contentWidth - 100)/2, 80, 100, 100);
        
        JLabel avatarText = new JLabel("H", SwingConstants.CENTER);
        avatarText.setFont(new Font("Segoe UI", Font.BOLD, 42)); avatarText.setForeground(Color.WHITE);
        avatarText.setBounds(0, 0, 100, 100); avatarCircle.add(avatarText);
        profileViewPanel.add(avatarCircle);
        
        JLabel usernameLabel = new JLabel("Hammad", SwingConstants.CENTER);
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24)); usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(midX, 195, contentWidth, 30); profileViewPanel.add(usernameLabel);
        
        JLabel tierLabel = new JLabel("👑 Premium Global Vanguard Explorer", SwingConstants.CENTER);
        tierLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12)); tierLabel.setForeground(new Color(0, 200, 255));
        tierLabel.setBounds(midX, 225, contentWidth, 20); profileViewPanel.add(tierLabel);
        
        int btnW1 = 160, btnW2 = 210, btnPadding = 15;
        int buttonsStartX = midX + (contentWidth - (btnW1 + btnPadding + btnW2)) / 2;
        
        JButton editProfileBtn = createStyledProfileButton("📝 Edit Profile Settings", new Color(255, 255, 255, 30), Color.WHITE);
        editProfileBtn.setBounds(buttonsStartX, 260, btnW1, 35);
        
        JButton logoutBtn = createStyledProfileButton("🚪 Terminate Session & Logout", new Color(255, 70, 70, 40), new Color(255, 100, 100));
        logoutBtn.setBounds(buttonsStartX + btnW1 + btnPadding, 260, btnW2, 35);
        
        profileViewPanel.add(editProfileBtn);
        profileViewPanel.add(logoutBtn);
        
        JPanel badge1 = createMilestoneBadgeCard("🧭 Wanderlust Status", "6 Countries Logged"); badge1.setBounds(midX, 370, 180, 75);
        profileViewPanel.add(badge1);
    }
    // =================================================================
    // UI DESIGN & COMPONENT CREATION TOOLKIT Helpers
    // =================================================================


    private void applyActiveMenuState(JButton button) {
        if (button == null) return;
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 200, 255), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setName("active");
    }

    private JPanel createDestinationCard(String city, String details, float rating, String imagePath) {
        final Image[] cardImage = {null};
        try {
            URL imgURL = getClass().getResource(imagePath);
            if (imgURL != null) cardImage[0] = new ImageIcon(imgURL).getImage();
        } catch (Exception e) {
            System.out.println("Image path unavailable: " + imagePath);
        }

        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 30)); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                if (cardImage[0] != null) {
                    g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), 100, 20, 20));
                    g2.drawImage(cardImage[0], 0, 0, getWidth(), 100, this);
                    g2.setClip(null);
                } else {
                    g2.setColor(new Color(0, 0, 0, 40)); g2.fillRoundRect(0, 0, getWidth(), 100, 20, 20);
                    g2.setColor(Color.LIGHT_GRAY); g2.drawString("🏨 Preview N/A", 20, 55);
                }
                g2.dispose();
            }
        };
        card.setLayout(null); card.setOpaque(false);
        
        JLabel name = new JLabel(city); name.setFont(new Font("Segoe UI", Font.BOLD, 14)); name.setForeground(Color.WHITE);
        name.setBounds(15, 112, 180, 20); card.add(name);
        
        JLabel desc = new JLabel(details); desc.setFont(new Font("Segoe UI", Font.PLAIN, 11)); desc.setForeground(new Color(210, 210, 210));
        desc.setBounds(15, 134, 180, 20); card.add(desc);

        JLabel stars = new JLabel("⭐ " + rating); stars.setFont(new Font("Segoe UI", Font.BOLD, 11)); stars.setForeground(new Color(255, 210, 50));
        stars.setBounds(15, 160, 80, 20); card.add(stars);
        
        return card;
    }

    private JPanel createBlankGlassViewScreen(String panelTitle) {
        JPanel view = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 25, 25);
                g2.dispose();
            }
        };
        view.setOpaque(false);
        JLabel label = new JLabel(panelTitle);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18)); label.setForeground(Color.WHITE);
        label.setBounds(20, 25, 400, 30); view.add(label);
        return view;
    }

    private void setupGlassTextFieldStyle(JTextField field) {
        field.setOpaque(false);
        field.setBackground(new Color(255, 255, 255, 15)); 
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13)); field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE); 
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 40), 1, true), 
            BorderFactory.createEmptyBorder(0, 12, 0, 12) 
        ));
    }

    private JPanel createTableRowLayout(String c1, String c2, String c3, boolean isHeaderRow) {
        JPanel row = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isHeaderRow ? new Color(255, 255, 255, 15) : new Color(255, 255, 255, 8));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
            }
        };
        row.setOpaque(false);
        JLabel l1 = new JLabel(c1); JLabel l2 = new JLabel(c2); JLabel l3 = new JLabel(c3);
        Font f = new Font("Segoe UI", isHeaderRow ? Font.BOLD : Font.PLAIN, 12);
        Color c = isHeaderRow ? new Color(0, 200, 255) : Color.WHITE;
        
        l1.setFont(f); l1.setForeground(c); l1.setBounds(15, 0, 280, 35);
        l2.setFont(f); l2.setForeground(c); l2.setBounds(310, 0, 160, 35);
        l3.setFont(f); l3.setForeground(c); l3.setBounds(480, 0, 110, 35);
        row.add(l1); row.add(l2); row.add(l3);
        return row;
    }

    private JButton createStyledProfileButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose(); super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 12)); button.setForeground(fg);
        button.setOpaque(false); button.setContentAreaFilled(false); button.setBorder(BorderFactory.createEmptyBorder());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JPanel createMilestoneBadgeCard(String header, String value) {
        JPanel card = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 12)); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        JLabel h = new JLabel(header, SwingConstants.CENTER); h.setFont(new Font("Segoe UI", Font.BOLD, 11)); h.setForeground(Color.LIGHT_GRAY); h.setBounds(0,15,180,15);
        JLabel v = new JLabel(value, SwingConstants.CENTER); v.setFont(new Font("Segoe UI", Font.BOLD, 14)); v.setForeground(new Color(0, 200, 255)); v.setBounds(0,38,180,25);
        card.add(h); card.add(v);
        return card;
    }

    private JPanel createListIndicatorCard(String t, String s) {
        JPanel p = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 20)); g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        JLabel title = new JLabel(t); title.setForeground(Color.WHITE); title.setBounds(15, 10, 450, 20);
        JLabel sub = new JLabel(s); sub.setForeground(Color.GRAY); sub.setBounds(15, 30, 450, 20);
        p.add(title); p.add(sub);
        return p;
    }
}
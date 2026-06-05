/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
    package com.travelsphere.gui;
    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.*;
    import java.lang.reflect.InvocationTargetException;
    import java.net.URL;
    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.ResultSet;
    import java.sql.PreparedStatement;
    import java.sql.SQLException;
/**
 *
 * @author Ahsan
 */
public class DashboardFram extends javax.swing.JFrame {
    
    
    // --- Database Configuration Configuration Variables ---
    private static final String DB_URL = "jdbc:mysql://localhost:3306/travelsphere_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    // --- UI Visual Elements Memory Cache ---
    private Image rawBackgroundImage = null;
    private JPanel backgroundLayer;
    private JLayeredPane glassCanvas; 
    
    private JPanel sidebarPanel;
    private JLabel logoLabel;
    private JButton dashboardBtn, bookingsBtn, exploreBtn, guidesBtn, settingsBtn, logoutBtn, gallaryBtn;
    private JButton hotelsBtn; 
    private JPanel hotelsViewPanel;
    private JPanel userAreaPanel;
    private JLabel avatarLabel, welcomeLabel, notificationLabel;
    
    // DISTINCT SCREENS
    private JPanel dashboardContentPanel;
    private JTextField centralizedSearchField;
    private JPanel exploreContentPanel;
    private JScrollPane exploreScrollPane;
    private JPanel destinationGrid; 
    private boolean isSidebarCollapsed = false;
    private JButton sidebarToggleBtn = new JButton();
    private JPanel travelTrendsWidget;
    private JLabel mapTrendsLabel;
    private JPanel recentSearchesWidget;
    private JScrollPane dashboardScrollPane;
    private JPanel dashboardScrollContent;
    private JLabel destCaption;
    private JLabel hotelCaption;
    private CardLayout contentDeckLayout;
    private JPanel centerContentDeck; 
    private JPanel bookingsViewPanel;
    private JPanel guidesViewPanel;
    private JPanel galleryViewPanel;
    private JPanel profileViewPanel;
    private int loggedInUserId;
    private JPanel hotelsGrid;
    private JScrollPane hotelsScrollPane;
    private JPopupMenu responsiveDropMenu;
  
public DashboardFram() {
    this(1); 
}
public DashboardFram(int authenticatedUserId) {
    this.loggedInUserId = authenticatedUserId; // Save the dynamic user ID globally
    
    initComponents();
    getContentPane().setLayout(null);
    initExploreGridComponent();
    inithotelsViewPanel();
    initProfileViewComponent(); // 🔍 Make sure your profile panel is initialized here!
    initPremiumDashboardLayout();
    initializeCentralizedSearchField();
    this.setSize(1050, 720);
    
    if (this.getComponentListeners().length > 0) {
        ComponentListener cl = this.getComponentListeners()[0];
        cl.componentResized(new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED));
    }
}
private void initPremiumDashboardLayout() {
    try {
        URL imgURL = getClass().getResource("/com/travelsphere/assets/icons/DashboardBackground.png");
        if (imgURL != null) {
            this.rawBackgroundImage = new ImageIcon(imgURL).getImage();
        }
    } catch (Exception e) {
        System.out.println("Background image asset path not found.");
    }

    backgroundLayer = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (rawBackgroundImage != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(rawBackgroundImage, 0, 0, getWidth(), getHeight(), this);
                g2.dispose();
            }
        }
    };
    backgroundLayer.setLayout(null);
    this.getContentPane().add(backgroundLayer);
    this.getContentPane().setComponentZOrder(backgroundLayer, 0);

    glassCanvas = new JLayeredPane() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 255, 255, 45)); 
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 35, 35);
            g2.setColor(new Color(255, 255, 255, 90)); 
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 35, 35);
            g2.dispose();
        }
    };
    glassCanvas.setOpaque(false);
    glassCanvas.setLayout(null);
    this.getLayeredPane().add(glassCanvas, JLayeredPane.PALETTE_LAYER);

    initSidebarComponent(glassCanvas);
    initTopStatusBarComponent(glassCanvas);

    contentDeckLayout = new CardLayout();
    centerContentDeck = new JPanel(contentDeckLayout);
    centerContentDeck.setOpaque(false);
    glassCanvas.add(centerContentDeck);

    // Build screens separate from one another
    initDashboardContentComponent();
    initExploreGridComponent(); 
    initBookingsViewComponent();
    inithotelsViewPanel();
    initGuidesViewComponent();
    initGalleryViewComponent();
    initProfileViewComponent();

    centerContentDeck.add(dashboardContentPanel, "DASHBOARD");
    centerContentDeck.add(exploreContentPanel, "EXPLORE");
    centerContentDeck.add(hotelsViewPanel, "HOTELS");
    centerContentDeck.add(bookingsViewPanel, "BOOKINGS");
    centerContentDeck.add(guidesViewPanel, "GUIDES");
    centerContentDeck.add(galleryViewPanel, "GALLERY");
    centerContentDeck.add(profileViewPanel, "PROFILE");

    initTravelTrendsComponent(glassCanvas);
    initRecentSearchesComponent(glassCanvas);

    initResponsiveLayoutListener(glassCanvas);

    this.setSize(1050, 720);
    this.setMinimumSize(new Dimension(900, 680));
    this.setLocationRelativeTo(null);
}
    private void initSidebarComponent(JLayeredPane glassPane) {
        sidebarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 20)); 
                g2.fillRoundRect(10, 10, getWidth() - 15, getHeight() - 20, 25, 25);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.drawRoundRect(10, 10, getWidth() - 16, getHeight() - 21, 25, 25);
                g2.dispose();
            }
        };
        sidebarPanel.setOpaque(false);
        sidebarPanel.setLayout(null);
        glassPane.add(sidebarPanel);
        sidebarToggleBtn = new JButton();
        try {
            java.net.URL iconUrl = getClass().getResource("/com/travelsphere/assets/icons/tooglebtn.png");
            if (iconUrl != null) {
                ImageIcon rawIcon = new ImageIcon(iconUrl);
                Image scaledImg = rawIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
                sidebarToggleBtn.setIcon(new ImageIcon(scaledImg));
            } else {
                sidebarToggleBtn.setText("☰");
            }
        } catch (Exception ex) {
            sidebarToggleBtn.setText("☰");
        }

        sidebarToggleBtn.setBorderPainted(false);
        sidebarToggleBtn.setContentAreaFilled(false);
        sidebarToggleBtn.setFocusPainted(false);
        sidebarToggleBtn.setOpaque(false);
        sidebarToggleBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        sidebarToggleBtn.setVisible(false);
        sidebarPanel.add(sidebarToggleBtn);

        logoLabel = new JLabel("TravelSphere");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logoLabel.setForeground(Color.WHITE);
        sidebarPanel.add(logoLabel);

        // Instantiating all menu items via glassmorphic UI factory generator
        dashboardBtn = createGlassMenuButton("Dashboard", "/com/travelsphere/assets/icons/dashboard.png");
        bookingsBtn = createGlassMenuButton("My Bookings", "/com/travelsphere/assets/icons/bookings.png");
        exploreBtn = createGlassMenuButton("Explore Destinations", "/com/travelsphere/assets/icons/explore.png");
        hotelsBtn = createGlassMenuButton("Hotels & Stays", "/com/travelsphere/assets/icons/hotel.png");
        guidesBtn = createGlassMenuButton("Travel Guides", "/com/travelsphere/assets/icons/guides.png");
        gallaryBtn = createGlassMenuButton("Gallary", "/com/travelsphere/assets/icons/gallary.png");
        settingsBtn = createGlassMenuButton("Profile Settings", "/com/travelsphere/assets/icons/settings.png");
        logoutBtn = createGlassMenuButton("Logout", "/com/travelsphere/assets/icons/logout.png");

        sidebarPanel.add(dashboardBtn);
        sidebarPanel.add(bookingsBtn);
        sidebarPanel.add(exploreBtn);
        sidebarPanel.add(hotelsBtn);
        sidebarPanel.add(guidesBtn);
        sidebarPanel.add(gallaryBtn);
        sidebarPanel.add(settingsBtn);
        sidebarPanel.add(logoutBtn);
        
        applyActiveMenuState(dashboardBtn);
        sidebarToggleBtn.addActionListener(e -> {
            responsiveDropMenu = new JPopupMenu();
            responsiveDropMenu.setBackground(new Color(25, 32, 43));
            responsiveDropMenu.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 40), 1));
            
            JMenuItem dashItem = createPopupMenuItem("Dashboard", "/com/travelsphere/assets/icons/dashboard.png");
            JMenuItem bookItem = createPopupMenuItem("My Bookings", "/com/travelsphere/assets/icons/bookings.png");
            JMenuItem explItem = createPopupMenuItem("Explore Destinations", "/com/travelsphere/assets/icons/explore.png");
            JMenuItem hoteItem = (hotelsBtn != null) ? createPopupMenuItem("Hotels & Stays", "/com/travelsphere/assets/icons/hotel.png") : null;
            JMenuItem guidItem = createPopupMenuItem("Travel Guides", "/com/travelsphere/assets/icons/guides.png");
            JMenuItem gallItem = createPopupMenuItem("Gallery", "/com/travelsphere/assets/icons/gallary.png");
            JMenuItem settItem = createPopupMenuItem("Profile Settings", "/com/travelsphere/assets/icons/settings.png");
            JMenuItem logoItem = createPopupMenuItem("Logout", "/com/travelsphere/assets/icons/logout.png");

            dashItem.addActionListener(ae -> switchActiveScreen(dashboardBtn, "DASHBOARD"));
            bookItem.addActionListener(ae -> switchActiveScreen(bookingsBtn, "BOOKINGS"));
            explItem.addActionListener(ae -> switchActiveScreen(exploreBtn, "EXPLORE"));
            if (hoteItem != null) hoteItem.addActionListener(ae -> switchActiveScreen(hotelsBtn, "HOTELS"));
            guidItem.addActionListener(ae -> switchActiveScreen(guidesBtn, "GUIDES"));
            gallItem.addActionListener(ae -> switchActiveScreen(gallaryBtn, "GALLERY"));
            settItem.addActionListener(ae -> switchActiveScreen(settingsBtn, "PROFILE"));
            
            logoItem.addActionListener(ae -> {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) this.dispose();
            });

            responsiveDropMenu.add(dashItem);
            responsiveDropMenu.add(bookItem);
            responsiveDropMenu.add(explItem);
            if (hoteItem != null) responsiveDropMenu.add(hoteItem);
            responsiveDropMenu.add(guidItem);
            responsiveDropMenu.add(gallItem);
            responsiveDropMenu.add(settItem);
            responsiveDropMenu.addSeparator();
            responsiveDropMenu.add(logoItem);

            responsiveDropMenu.show(sidebarToggleBtn, 0, sidebarToggleBtn.getHeight() + 5);
        });

        dashboardBtn.addActionListener(e -> switchActiveScreen(dashboardBtn, "DASHBOARD"));
        exploreBtn.addActionListener(e -> switchActiveScreen(exploreBtn, "EXPLORE"));
        hotelsBtn.addActionListener(e -> switchActiveScreen(hotelsBtn, "HOTELS"));
        bookingsBtn.addActionListener(e -> switchActiveScreen(bookingsBtn, "BOOKINGS"));
        guidesBtn.addActionListener(e -> switchActiveScreen(guidesBtn, "GUIDES"));
        gallaryBtn.addActionListener(e -> switchActiveScreen(gallaryBtn, "GALLERY"));
        settingsBtn.addActionListener(e -> switchActiveScreen(settingsBtn, "PROFILE"));

        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
            }
        });
    }
    

private boolean ignoreNextReset = false;

private void initializeCentralizedSearchField() {
    centralizedSearchField = new JTextField("Search Destinations, Hotels, & Experiences...") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 255, 255, 50));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    };

    setupGlassTextFieldStyle(centralizedSearchField);
    
    getContentPane().add(centralizedSearchField);
    getContentPane().setComponentZOrder(centralizedSearchField, 0);

    // 🌟 ACTION LISTENER (FIRES ONLY ON ENTER KEY)
    centralizedSearchField.addActionListener((ActionEvent e) -> {
        String input = centralizedSearchField.getText().trim();
        
        if (input.equals("Search Destinations, Hotels, & Experiences...") ||
                input.equals("Search luxury hotels, resorts, and villas...") || 
                input.isEmpty()) {
            return;
        }
        
        // 🌟 LOG HISTORY PATHWAY: Commit the valid query to your phpMyAdmin database table
        logRecentSearch(input);
        
        if (exploreContentPanel != null && exploreContentPanel.isVisible() && destinationGrid != null) {
            loadDestinationsFromDatabase(destinationGrid, 24, input);
            
            int currentW = DashboardFram.this.getWidth();
            int currentH = DashboardFram.this.getHeight();
            DashboardFram.this.setSize(currentW + 1, currentH);
            DashboardFram.this.setSize(currentW, currentH);
        }
        
        if (hotelsViewPanel != null && hotelsViewPanel.isVisible() && hotelsGrid != null) {
            loadHotelsFromDatabase(hotelsGrid, input);
            
            int currentW = DashboardFram.this.getWidth();
            int currentH = DashboardFram.this.getHeight();
            DashboardFram.this.setSize(currentW + 1, currentH);
            DashboardFram.this.setSize(currentW, currentH);
        }

        if (dashboardContentPanel != null && dashboardContentPanel.isVisible() && dashboardScrollContent != null) {
            JPanel tempDestPanel = new JPanel();
            JPanel tempHotelPanel = new JPanel();
            
            loadDestinationsFromDatabase(tempDestPanel, 3, input);
            loadHotelsFromDatabase(tempHotelPanel, input);
            
            dashboardScrollContent.removeAll();
            
            if (destCaption != null) dashboardScrollContent.add(destCaption);
            if (hotelCaption != null) dashboardScrollContent.add(hotelCaption);
            
            for (Component c : tempDestPanel.getComponents()) {
                dashboardScrollContent.add(c);
            }
            for (Component c : tempHotelPanel.getComponents()) {
                dashboardScrollContent.add(c);
            }
            
            int currentW = DashboardFram.this.getWidth();
            int currentH = DashboardFram.this.getHeight();
            DashboardFram.this.setSize(currentW + 1, currentH);
            DashboardFram.this.setSize(currentW, currentH);
        }
    });

    centralizedSearchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        private void checkForReset() {
            SwingUtilities.invokeLater(() -> {
                if (ignoreNextReset) {
                    ignoreNextReset = false; 
                    return;
                }

                String input = centralizedSearchField.getText().trim();
                
                if (input.isEmpty()) {
                    if (exploreContentPanel != null && exploreContentPanel.isVisible() && destinationGrid != null) {
                        loadDestinationsFromDatabase(destinationGrid, 24, "");
                    }
                    if (hotelsViewPanel != null && hotelsViewPanel.isVisible() && hotelsGrid != null) {
                        loadHotelsFromDatabase(hotelsGrid, "");
                    }
                    
                    if (dashboardContentPanel != null && dashboardContentPanel.isVisible() && dashboardScrollContent != null) {
                        JPanel tempDestPanel = new JPanel();
                        JPanel tempHotelPanel = new JPanel();
                        
                        loadDestinationsFromDatabase(tempDestPanel, 3, "");
                        loadHotelsFromDatabase(tempHotelPanel, "");
                        
                        dashboardScrollContent.removeAll();
                        if (destCaption != null) dashboardScrollContent.add(destCaption);
                        if (hotelCaption != null) dashboardScrollContent.add(hotelCaption);
                        
                        for (Component c : tempDestPanel.getComponents()) {
                            dashboardScrollContent.add(c);
                        }
                        for (Component c : tempHotelPanel.getComponents()) {
                            dashboardScrollContent.add(c);
                        }
                    }
                    
                    int currentW = DashboardFram.this.getWidth();
                    int currentH = DashboardFram.this.getHeight();
                    DashboardFram.this.setSize(currentW + 1, currentH);
                    DashboardFram.this.setSize(currentW, currentH);
                }
            });
        }
        @Override 
        public void insertUpdate(javax.swing.event.DocumentEvent e){ 
            checkForReset(); 
        }
        @Override 
        public void removeUpdate(javax.swing.event.DocumentEvent e){ 
            checkForReset(); 
        }
        @Override public void changedUpdate(javax.swing.event.DocumentEvent e){ 
            checkForReset(); 
        }
    });

    centralizedSearchField.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            String txt = centralizedSearchField.getText().trim();
            if (txt.equals("Search Destinations, Hotels, & Experiences...") || 
                txt.equals("Search luxury hotels, resorts, and villas...")) {
                
                ignoreNextReset = true; 
                centralizedSearchField.setText("");
            }
            centralizedSearchField.setForeground(Color.WHITE);
        }
        
        @Override
        public void focusLost(FocusEvent e) {
            if (centralizedSearchField.getText().trim().isEmpty()) {
                ignoreNextReset = true; 
                if (hotelsViewPanel != null && hotelsViewPanel.isVisible()) {
                    centralizedSearchField.setText("Search luxury hotels, resorts, and villas...");
                } else {
                    centralizedSearchField.setText("Search Destinations, Hotels, & Experiences...");
                }
                centralizedSearchField.setForeground(new Color(220, 220, 220));
            }
        }
    });
}
    private void initTopStatusBarComponent(JLayeredPane glassPane) {
        userAreaPanel = new JPanel();
        userAreaPanel.setOpaque(false);
        userAreaPanel.setLayout(null);
        glassPane.add(userAreaPanel);

        welcomeLabel = new JLabel("Welcome, Hammad!", SwingConstants.RIGHT);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        welcomeLabel.setForeground(Color.WHITE);
        userAreaPanel.add(welcomeLabel);

        avatarLabel = new JLabel();
        avatarLabel.setOpaque(true);
        avatarLabel.setBackground(new Color(255, 255, 255, 60));
        userAreaPanel.add(avatarLabel);

        notificationLabel = new JLabel("   🔔", SwingConstants.CENTER);
        notificationLabel.setForeground(Color.WHITE);
        userAreaPanel.add(notificationLabel);
    }
private JPanel dashboardHeaderArea;

private void initDashboardContentComponent(){
   
    dashboardContentPanel = new JPanel();
    dashboardContentPanel.setOpaque(false);
    dashboardContentPanel.setLayout(null);

    dashboardHeaderArea = new JPanel();
    dashboardHeaderArea.setOpaque(false);
    dashboardHeaderArea.setLayout(null);
    dashboardContentPanel.add(dashboardHeaderArea);

    JLabel header = new JLabel("Welcome TravelSphere, Discover Something New With Us!!!");
    header.setFont(new Font("Segoe UI", Font.BOLD, 20));
    header.setForeground(Color.WHITE);
    dashboardHeaderArea.add(header);

    dashboardScrollContent = new JPanel();
    dashboardScrollContent.setOpaque(false);
    dashboardScrollContent.setLayout(null); 

    dashboardScrollPane = new JScrollPane(dashboardScrollContent);
    dashboardScrollPane.setOpaque(false);
    dashboardScrollPane.getViewport().setOpaque(false);
    
    dashboardScrollPane.setBorder(BorderFactory.createEmptyBorder());
    dashboardScrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
    dashboardScrollPane.getVerticalScrollBar().setBorder(BorderFactory.createEmptyBorder());
    dashboardScrollPane.getVerticalScrollBar().setOpaque(false);
    dashboardScrollPane.getVerticalScrollBar().setUnitIncrement(18); 
    dashboardScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    dashboardContentPanel.add(dashboardScrollPane);

    destCaption = new JLabel("TRENDING DESTINATIONS");
    destCaption.setFont(new Font("Segoe UI", Font.BOLD, 15));
    destCaption.setForeground(new Color(0, 210, 255)); 
    dashboardScrollContent.add(destCaption);

    hotelCaption = new JLabel("EXQUISITE LUXURY STAYS");
    hotelCaption.setFont(new Font("Segoe UI", Font.BOLD, 15));
    hotelCaption.setForeground(new Color(0, 210, 255)); 
    dashboardScrollContent.add(hotelCaption);

    loadDestinationsFromDatabase(dashboardScrollContent, 3, "");
    loadHotelsFromDatabase(dashboardScrollContent, ""); 
}

    private void initExploreGridComponent() {

    exploreContentPanel = createBlankGlassViewScreen("Explore Destinations");
    exploreContentPanel.setLayout(null);
    destinationGrid = new JPanel();
    destinationGrid.setOpaque(false);
    destinationGrid.setLayout(null);

    loadDestinationsFromDatabase(destinationGrid, 24, "");

    exploreScrollPane = new JScrollPane(destinationGrid);
    exploreScrollPane.setOpaque(false);
    exploreScrollPane.getViewport().setOpaque(false);
    exploreScrollPane.setBorder(null);
    exploreScrollPane.setViewportBorder(null);
    exploreScrollPane.getVerticalScrollBar().setUnitIncrement(16);
    
    exploreScrollPane.setBounds(25, 170, 550, 350);
    exploreContentPanel.add(exploreScrollPane);
}

    private void initTravelTrendsComponent(JLayeredPane glassPane) {
    travelTrendsWidget = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255, 255, 255, 25)); // Smooth frosted glass backdrop
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            g2.dispose();
        }
    };
    travelTrendsWidget.setOpaque(false);
    travelTrendsWidget.setLayout(null); // Keeps your exact absolute positioning engine intact
    glassPane.add(travelTrendsWidget);

    // 1. Static Section Header Title
    JLabel title = new JLabel("Active Travel Trends");
    title.setFont(new Font("Segoe UI", Font.BOLD, 16));
    title.setForeground(new Color(0, 210, 255));
    // Explicitly position the title inside the null layout boundary
    title.setBounds(20, 15, 200, 25);
    travelTrendsWidget.add(title);

    // 2. Create the vertical list panel to stack our 5 TrendRowItems
    JPanel listContainer = new JPanel();
    listContainer.setOpaque(false);
    listContainer.setLayout(new BoxLayout(listContainer, BoxLayout.Y_AXIS));

    // 3. Wrap it in a scroll pane to handle small window resolutions elegantly
    JScrollPane widgetScroll = new JScrollPane(listContainer);
    widgetScroll.setOpaque(false);
    widgetScroll.getViewport().setOpaque(false);
    widgetScroll.setBorder(null);
    widgetScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    widgetScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    widgetScroll.getVerticalScrollBar().setUnitIncrement(12);

    // 4. 🛠️ HANDLING THE ABSOLUTE BOUNDS (The Component Resizer)
    // Since the widget layout is null, we track the widget's resize events 
    // to dynamically stretch our scroll pane to 100% of the available width/height
    travelTrendsWidget.addComponentListener(new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            int w = travelTrendsWidget.getWidth();
            int h = travelTrendsWidget.getHeight();
            if (w > 0 && h > 0) {
                // Places the list scrollbar box exactly 50px below the top title, spanning the full width
                widgetScroll.setBounds(10, 50, w - 20, h - 65);
            }
        }
    });
    travelTrendsWidget.add(widgetScroll);

    // 5. Run the initial data loading pipeline to inject the rows right out of the gate
    loadTravelTrendsData(listContainer);
}

private void initRecentSearchesComponent(JLayeredPane glassPane) {
    // Clean, transparent container that adopts your exact dashboard layout coordinates
    recentSearchesWidget = new JPanel();
    recentSearchesWidget.setOpaque(false);
    recentSearchesWidget.setLayout(null); // Absolute positioning engine remains intact
    
    glassPane.add(recentSearchesWidget);

    // Initial query pass to load and render the data carousel right away
    loadRecentSearchesData();
}

    private void initResponsiveLayoutListener(JLayeredPane glassPane) {
        this.addComponentListener(new ComponentAdapter() {
            @Override

public void componentResized(ComponentEvent e) {
    int width = getContentPane().getWidth();
    int height = getContentPane().getHeight();

    if (width <= 0 || height <= 0) return;

    backgroundLayer.setBounds(0, 0, width, height);

    int margin = 20; 
    int canvasW = width - (margin * 2);
    int canvasH = height - (margin * 2);
    glassPane.setBounds(margin, margin, canvasW, canvasH);

    // 🛠️ DYNAMIC RESPONSIVE SIDEBAR ENGINE (< 1000px Collapses)
    int sidebarW = (width < 1000) ? 65 : 220; 
    isSidebarCollapsed = (sidebarW == 65);
    int remainingW = canvasW - sidebarW;

    int centerColumnW;
    int rightColumnW;

    boolean isDashboardActive = travelTrendsWidget != null && travelTrendsWidget.isVisible();

    if (isDashboardActive) {
        centerColumnW = (int)(remainingW * 0.72); 
        rightColumnW = remainingW - centerColumnW - 25; 
    } else {
        centerColumnW = remainingW - 20; 
        rightColumnW = 0;
    }

    sidebarPanel.setBounds(0, 0, sidebarW, canvasH);

    if (isSidebarCollapsed) {
        logoLabel.setVisible(false);
        if (sidebarToggleBtn != null) {
            sidebarToggleBtn.setBounds(12, 20, 40, 35);
            sidebarToggleBtn.setVisible(true);
            sidebarPanel.setComponentZOrder(sidebarToggleBtn, 0);
        }

        // HIDE FULL BUTTONS WHEN COLLAPSED TO PREVENT OVERLAP ARTIFACTS
        dashboardBtn.setVisible(false);
        bookingsBtn.setVisible(false);
        exploreBtn.setVisible(false);
        if (hotelsBtn != null) hotelsBtn.setVisible(false);
        guidesBtn.setVisible(false);
        gallaryBtn.setVisible(false);
        settingsBtn.setVisible(false);
        logoutBtn.setVisible(false);

    } else {
        if (sidebarToggleBtn != null) {
            sidebarToggleBtn.setVisible(false);
        }
        logoLabel.setBounds(25, 20, sidebarW - 50, 35); 
        logoLabel.setText("TravelSphere");
        logoLabel.setVisible(true);

        // 🛠️ RESTORE FULL BUTTONS VISIBILITY WHEN EXPANDED
        dashboardBtn.setVisible(true);
        bookingsBtn.setVisible(true);
        exploreBtn.setVisible(true);
        if (hotelsBtn != null) hotelsBtn.setVisible(true);
        guidesBtn.setVisible(true);
        gallaryBtn.setVisible(true);
        settingsBtn.setVisible(true);
        logoutBtn.setVisible(true);
    }

    if (!isSidebarCollapsed) {
        int startY = 85, gap = 4, itemH = 38; 
        int btnMargin = 15;
        int btnW = sidebarW - (btnMargin * 2);

        dashboardBtn.setBounds(btnMargin, startY, btnW, itemH);
        bookingsBtn.setBounds(btnMargin, startY + (itemH + gap), btnW, itemH);
        exploreBtn.setBounds(btnMargin, startY + 2 * (itemH + gap), btnW, itemH);

        if (hotelsBtn != null) {
            hotelsBtn.setBounds(btnMargin, startY + 3 * (itemH + gap), btnW, itemH);
        }

        guidesBtn.setBounds(btnMargin, startY + 4 * (itemH + gap), btnW, itemH);
        gallaryBtn.setBounds(btnMargin, startY + 5 * (itemH + gap), btnW, itemH);
        settingsBtn.setBounds(btnMargin, startY + 6 * (itemH + gap), btnW, itemH);

        logoutBtn.setBounds(btnMargin, canvasH - 60, btnW, itemH);
    }

    userAreaPanel.setBounds(sidebarW, 0, canvasW - sidebarW, 75);
    int topBarW = userAreaPanel.getWidth();
    avatarLabel.setBounds(topBarW - 55, 20, 35, 35);
    int bellX = topBarW - 105;
    notificationLabel.setBounds(bellX, 20, 35, 35);
    welcomeLabel.setBounds(bellX - 210, 20, 200, 35);

    if (centerContentDeck != null) {
        centerContentDeck.setBounds(sidebarW, 75, centerColumnW, canvasH - 75);
    }

    if (dashboardContentPanel != null) dashboardContentPanel.setBounds(0, 0, centerColumnW, canvasH - 75);
    if (exploreContentPanel != null) exploreContentPanel.setBounds(0, 0, centerColumnW, canvasH - 75);

    // 🌟 HOTELS & STAYS VIEW RE-CALCULATION
    if (hotelsViewPanel != null) {
        hotelsViewPanel.setBounds(0, 0, centerColumnW, canvasH - 75);

        if (hotelsViewPanel.isVisible()) {
            int leftMarginPadding = 25;

            if (hotelsViewPanel.getComponentCount() > 0 && hotelsViewPanel.getComponent(0) instanceof JLabel) {
                hotelsViewPanel.getComponent(0).setBounds(leftMarginPadding, 15, centerColumnW - 50, 25);
            }

            if (centralizedSearchField != null) {
                if (centralizedSearchField.getParent() != hotelsViewPanel) {
                    hotelsViewPanel.add(centralizedSearchField);
                }
                centralizedSearchField.setBounds(leftMarginPadding, 55, centerColumnW - 50, 42);
                centralizedSearchField.setVisible(true);
            }

            int gridTopY = 115; 
            if (hotelsScrollPane != null && hotelsGrid != null) {
                hotelsScrollPane.setBounds(20, gridTopY, centerColumnW - 30, canvasH - 75 - gridTopY);

                int scrollW = hotelsScrollPane.getWidth() - 20;
                if (scrollW <= 0) {
                    scrollW = (centerColumnW - 30) - 20;
                }

                int padding = 12;
                int cardW = (scrollW - (padding * 3)) / 4; 

                int cardH = Math.min((int) (cardW * 1.10), 240);

                int totalItems = hotelsGrid.getComponentCount();
                int rows = (int) Math.ceil(totalItems / 4.0);

                int calculatedHeight = (rows * cardH) + ((rows - 1) * padding) + 20;
                int totalHeight = Math.max(calculatedHeight, hotelsScrollPane.getHeight() - 10);

                hotelsGrid.setBounds(0, 0, scrollW, totalHeight);
                hotelsGrid.setPreferredSize(new Dimension(scrollW, totalHeight));

                for (int i = 0; i < totalItems; i++) {
                    int r = i / 4;
                    int c = i % 4;
                    hotelsGrid.getComponent(i).setBounds(c * (cardW + padding), r * (cardH + padding), cardW, cardH);
                }
            }
        }
    }

    if (bookingsViewPanel != null) bookingsViewPanel.setBounds(0, 0, centerColumnW, canvasH - 75);
    if (guidesViewPanel != null) guidesViewPanel.setBounds(0, 0, centerColumnW, canvasH - 75);
    if (galleryViewPanel != null) galleryViewPanel.setBounds(0, 0, centerColumnW, canvasH - 75);
    if (profileViewPanel != null) profileViewPanel.setBounds(0, 0, centerColumnW, canvasH - 75);

    // 🌟 MAIN DASHBOARD LAYOUT ENGINE
    if (dashboardContentPanel != null && dashboardContentPanel.isVisible()) {
        int usableW = centerColumnW - 20;

        if (dashboardHeaderArea != null) {
            dashboardHeaderArea.setBounds(0, 0, centerColumnW, 110);
            if (dashboardHeaderArea.getComponentCount() > 0) {
                dashboardHeaderArea.getComponent(0).setBounds(15, 15, usableW - 20, 25); 
            }

            if (centralizedSearchField != null) {
                if (centralizedSearchField.getParent() != dashboardHeaderArea) {
                    dashboardHeaderArea.add(centralizedSearchField);
                }
                centralizedSearchField.setBounds(15, 55, usableW - 20, 42); 
                centralizedSearchField.setVisible(true);
            }
        }

        int gridTopY = 115;
        int scrollAreaH = canvasH - 75 - gridTopY;
        if (dashboardScrollPane != null) {
            dashboardScrollPane.setBounds(10, gridTopY, centerColumnW - 25, scrollAreaH);
        }

        if (dashboardScrollContent != null) {
            Component[] children = dashboardScrollContent.getComponents();

            java.util.List<Component> destinationCardsList = new java.util.ArrayList<>();
            java.util.List<Component> hotelCardsList = new java.util.ArrayList<>();

            for (Component comp : children) {
                if (comp == destCaption || comp == hotelCaption || comp == centralizedSearchField) continue;

                if (comp instanceof JLabel lbl) {
                    if (lbl.getText() != null && lbl.getText().contains("Welcome TravelSphere")) {
                        comp.setBounds(0, 0, 0, 0);
                        continue;
                    }
                }

                boolean isHotel = false;
                if (comp instanceof Container container) {
                    for (Component child : container.getComponents()) {
                        if (child instanceof JLabel label && label.getText() != null) {
                            String txt = label.getText().toLowerCase();
                            if (txt.contains("night") || txt.contains("$") || txt.contains("price")) {
                                isHotel = true;
                                break;
                            }
                        }
                    }
                }

                if (isHotel) {
                    if (!hotelCardsList.contains(comp)) hotelCardsList.add(comp);
                } else {
                    if (!destinationCardsList.contains(comp)) destinationCardsList.add(comp);
                }
            }
            if (destinationCardsList.isEmpty() && dashboardContentPanel.isVisible()) {
                dashboardScrollContent.removeAll();
                loadDestinationsFromDatabase(dashboardScrollContent, 3, "");

                for (Component comp : dashboardScrollContent.getComponents()) {
                    if (comp != destCaption && comp != hotelCaption && comp != centralizedSearchField) {
                        destinationCardsList.add(comp);
                    }
                }

                if (destCaption != null) dashboardScrollContent.add(destCaption);
                if (hotelCaption != null) dashboardScrollContent.add(hotelCaption);
                for (Component hotel : hotelCardsList) {
                    dashboardScrollContent.add(hotel);
                }
                children = dashboardScrollContent.getComponents();
            }

            // 🛠️ DYNAMIC LISTENERS ATTACHMENT ENGINE WITH AUTOMATED PRIMARY KEY RECOVERY
            int indexCounter = 0;
            for (Component card : children) {
                if (card instanceof JPanel cardPanel && card != destCaption && card != hotelCaption && card != centralizedSearchField) {
                    indexCounter++;

                    for (java.awt.event.MouseListener ml : cardPanel.getMouseListeners()) {
                        cardPanel.removeMouseListener(ml);
                    }

                    boolean isHotelCard = false;
                    String detectedName = "";

                    for (Component child : cardPanel.getComponents()) {
                        if (child instanceof JLabel lbl) {
                            String txt = lbl.getText();
                            if (txt != null && !txt.isEmpty()) {
                                if (txt.toLowerCase().contains("night") || txt.contains("$") || txt.toLowerCase().contains("price")) {
                                    isHotelCard = true;
                                }
                                if (detectedName.isEmpty() && !txt.contains("4.") && !txt.contains("5.")) {
                                    detectedName = txt.trim();
                                }
                            }
                        }
                    }

                    // Extract assigned database layout IDs dynamically
                    Object storedId = cardPanel.getClientProperty("databaseId");
                    final int runtimeId = (storedId instanceof Integer) ? (Integer) storedId : indexCounter;

                    final String finalName = detectedName;
                    final boolean finalIsHotel = isHotelCard;
                    final javax.swing.border.Border originalBorder = cardPanel.getBorder();
                    final javax.swing.border.Border hoverBorder = javax.swing.BorderFactory.createLineBorder(new Color(102, 204, 255, 160), 2, true);

                    java.awt.event.MouseAdapter cardHoverAdapter = new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            for (Component comp : centerContentDeck.getComponents()) {
                                if (comp instanceof ItemDetailPanel) {
                                    centerContentDeck.remove(comp);
                                }
                            }

                            // 🎯 FIXED: Replaces structural hardcoded '1' with recovered dynamic tracking index 
                            ItemDetailPanel detailView = new ItemDetailPanel(
                                DashboardFram.this,
                                centerContentDeck,
                                contentDeckLayout,
                                finalName,
                                runtimeId, 
                                finalIsHotel,
                                "DASHBOARD"
                            );

                            centerContentDeck.add(detailView, "detailView");
                            contentDeckLayout.show(centerContentDeck, "detailView");

                            centerContentDeck.revalidate();
                            centerContentDeck.repaint();
                        }

                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent evt) {
                            cardPanel.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
                            cardPanel.setBorder(hoverBorder); 
                            cardPanel.repaint();
                        }

                        @Override
                        public void mouseExited(java.awt.event.MouseEvent evt) {
                            cardPanel.setBorder(originalBorder);
                            cardPanel.repaint();
                        }
                    };
                    cardPanel.addMouseListener(cardHoverAdapter);
                    for (Component child : cardPanel.getComponents()) {
                        for (java.awt.event.MouseListener ml : child.getMouseListeners()) {
                            child.removeMouseListener(ml);
                        }
                        child.addMouseListener(cardHoverAdapter);
                    }
                }
            }

            int scrollInnerW = dashboardScrollPane.getWidth() - 16;
            if (scrollInnerW <= 0) scrollInnerW = usableW;

            int padding = 12;
            int cardW = (scrollInnerW - (padding * 2)) / 3; 
            int cardH = Math.min((int) (cardW * 1.10), 240);

            int destHeaderY = 10;
            if (destCaption != null) {
                destCaption.setBounds(15, destHeaderY, scrollInnerW - 30, 25);
            }

            int destCardsY = destHeaderY + 35;
            int hotelHeaderY = destCardsY + cardH + 35; 

            int destCount = 0;
            for (Component destCard : destinationCardsList) {
                if (dashboardScrollContent != destCard.getParent()) {
                    dashboardScrollContent.add(destCard);
                }
                if (destCount < 3) {
                    destCard.setBounds(15 + destCount * (cardW + padding), destCardsY, cardW, cardH);
                    destCard.setVisible(true);
                    destCount++;
                } else {
                    destCard.setBounds(0, 0, 0, 0);
                }
            }

            int hotelCount = 0;
            for (Component hotelCard : hotelCardsList) {
                if (dashboardScrollContent != hotelCard.getParent()) {
                    dashboardScrollContent.add(hotelCard);
                }
                if (hotelCount < 3) {
                    hotelCard.setBounds(15 + hotelCount * (cardW + padding), hotelHeaderY + 35, cardW, cardH);
                    hotelCard.setVisible(true);
                    hotelCount++;
                } else {
                    hotelCard.setBounds(0, 0, 0, 0);
                }
            }

            if (hotelCaption != null) {
                hotelCaption.setBounds(15, hotelHeaderY, scrollInnerW - 30, 25);
            }

            int totalContentHeight = hotelHeaderY + 35 + cardH + 25;
            dashboardScrollContent.setPreferredSize(new Dimension(scrollInnerW, totalContentHeight));
            dashboardScrollContent.setBounds(0, 0, scrollInnerW, totalContentHeight);
        }
    }

    if (exploreContentPanel != null && exploreContentPanel.isVisible()) {
        int leftMarginPadding = 25;
        exploreContentPanel.getComponent(0).setBounds(leftMarginPadding, 15, centerColumnW - 50, 25);

        if (centralizedSearchField != null) {
            if (centralizedSearchField.getParent() != exploreContentPanel) {
                exploreContentPanel.add(centralizedSearchField);
            }
            centralizedSearchField.setBounds(leftMarginPadding, 55, centerColumnW - 50, 42);
            centralizedSearchField.setVisible(true);
        }

        int gridTopY = 115;
        exploreScrollPane.setBounds(20, gridTopY, centerColumnW - 30, canvasH - 75 - gridTopY);

        int scrollW = exploreScrollPane.getWidth() - 38; 
        if (scrollW <= 0) {
            scrollW = (centerColumnW - 30) - 38;
        }

        int padding = 12;
        int cardW = (scrollW - (padding * 3)) / 4; 
        int cardH = Math.min((int) (cardW * 1.10), 240);

        int totalItems = destinationGrid.getComponentCount();
        int rows = (int) Math.ceil(totalItems / 4.0);

        int calculatedHeight = (rows * cardH) + ((rows - 1) * padding) + 20;
        int totalHeight = Math.max(calculatedHeight, canvasH - 75 - gridTopY - 10);

        destinationGrid.setBounds(0, 0, scrollW, totalHeight);
        destinationGrid.setPreferredSize(new Dimension(scrollW, totalHeight));

        for (int i = 0; i < totalItems; i++) {
            int r = i / 4; int c = i % 4;
            destinationGrid.getComponent(i).setBounds(c * (cardW + padding), r * (cardH + padding), cardW, cardH);

            // 🛠️ DYNAMIC EXTRACTION ENGINE FOR EXPLORE CATALOG SECTION
            Component exploreCard = destinationGrid.getComponent(i);
            if (exploreCard instanceof JPanel exploreCardPanel) {
                for (java.awt.event.MouseListener ml : exploreCardPanel.getMouseListeners()) {
                    exploreCardPanel.removeMouseListener(ml);
                }

                String detectedExpName = "";
                for (Component child : exploreCardPanel.getComponents()) {
                    if (child instanceof JLabel lbl) {
                        String txt = lbl.getText();
                        if (txt != null && !txt.isEmpty() && !txt.contains("4.") && !txt.contains("5.")) {
                            detectedExpName = txt.trim();
                            break;
                        }
                    }
                }

                Object storedId = exploreCardPanel.getClientProperty("databaseId");
                final int runtimeId = (storedId instanceof Integer) ? (Integer) storedId : (i + 1);

                final String finalExpName = detectedExpName;
                java.awt.event.MouseAdapter exploreClickAdapter = new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        for (Component comp : centerContentDeck.getComponents()) {
                            if (comp instanceof ItemDetailPanel) {
                                centerContentDeck.remove(comp);
                            }
                        }

                        // 🎯 FIXED: Dynamic ID implementation tracking instead of '1'
                        ItemDetailPanel detailView = new ItemDetailPanel(
                            DashboardFram.this, centerContentDeck, contentDeckLayout,
                            finalExpName, runtimeId, false, "EXPLORE"
                        );

                        centerContentDeck.add(detailView, "detailView");
                        contentDeckLayout.show(centerContentDeck, "detailView");
                        centerContentDeck.revalidate();
                        centerContentDeck.repaint();
                    }
                };
                exploreCardPanel.addMouseListener(exploreClickAdapter);
                for (Component child : exploreCardPanel.getComponents()) {
                    for (java.awt.event.MouseListener ml : child.getMouseListeners()) {
                        child.removeMouseListener(ml);
                    }
                    child.addMouseListener(exploreClickAdapter);
                }
            }
        }
    }

    // 🌟 DYNAMIC MODIFICATION: HOTELS VIEW MOUSE ADAPTER RE-BINDING
    if (hotelsViewPanel != null && hotelsViewPanel.isVisible() && hotelsGrid != null) {
        int totalHotels = hotelsGrid.getComponentCount();
        for (int i = 0; i < totalHotels; i++) {
            Component hotelCard = hotelsGrid.getComponent(i);
            if (hotelCard instanceof JPanel hotelCardPanel) {
                for (java.awt.event.MouseListener ml : hotelCardPanel.getMouseListeners()) {
                    hotelCardPanel.removeMouseListener(ml);
                }

                String detectedHotelName = "";
                for (Component child : hotelCardPanel.getComponents()) {
                    if (child instanceof JLabel lbl) {
                        String txt = lbl.getText();
                        if (txt != null && !txt.isEmpty() && !txt.contains("3.") && !txt.contains("5.") && !txt.contains("7.") && !txt.contains("$") && !txt.contains("Price")) {
                            detectedHotelName = txt.trim();
                            break;
                        }
                    }
                }

                Object storedId = hotelCardPanel.getClientProperty("databaseId");
                final int runtimeId = (storedId instanceof Integer) ? (Integer) storedId : (i + 1);

                final String finalHotelName = detectedHotelName;
                java.awt.event.MouseAdapter hotelClickAdapter = new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        for (Component comp : centerContentDeck.getComponents()) {
                            if (comp instanceof ItemDetailPanel) {
                                centerContentDeck.remove(comp);
                            }
                        }

                        // 🎯 FIXED: Dynamic ID assignment tracking instead of '1'
                        ItemDetailPanel detailView = new ItemDetailPanel(
                            DashboardFram.this, centerContentDeck, contentDeckLayout,
                            finalHotelName, runtimeId, true, "HOTELS"
                        );

                        centerContentDeck.add(detailView, "detailView");
                        contentDeckLayout.show(centerContentDeck, "detailView");
                        centerContentDeck.revalidate();
                        centerContentDeck.repaint();
                    }
                };
                hotelCardPanel.addMouseListener(hotelClickAdapter);
                for (Component child : hotelCardPanel.getComponents()) {
                    for (java.awt.event.MouseListener ml : child.getMouseListeners()) {
                        child.removeMouseListener(ml);
                    }
                    child.addMouseListener(hotelClickAdapter);
                }
            }
        }
    }

    if (isDashboardActive) {
        int rightX = sidebarW + centerColumnW + 10;
        int startTopY = 115; 

        int availableH = canvasH - startTopY - 25; 
        int spacingGap = 15;                

        int trendsH = (int) (availableH * 0.46);        
        int recentH = availableH - trendsH - spacingGap; 

        int maxAllowedH = (int) (rightColumnW * 1.15); 
        int minAllowedH = (int) (rightColumnW * 0.60); 

        trendsH = Math.max(minAllowedH, Math.min(trendsH, maxAllowedH));
        recentH = availableH - trendsH - spacingGap;

        if (travelTrendsWidget != null) {
            travelTrendsWidget.setBounds(rightX, startTopY, rightColumnW, trendsH);
            if (travelTrendsWidget.getComponentCount() > 0) {
                travelTrendsWidget.getComponent(0).setBounds(15, 15, rightColumnW - 30, 20);
            }
            if (mapTrendsLabel != null) {
                mapTrendsLabel.setBounds(15, 45, rightColumnW - 30, trendsH - 60);
            }
        }
        if (recentSearchesWidget != null) {
            int recentY = startTopY + trendsH + spacingGap;
            recentSearchesWidget.setBounds(rightX, recentY, rightColumnW, recentH);
            if (recentSearchesWidget.getComponentCount() > 0) {
                recentSearchesWidget.getComponent(0).setBounds(15, 15, rightColumnW - 30, 20);
            }
        }
        if (centerContentDeck != null) {
            getContentPane().setComponentZOrder(centerContentDeck, 0);
        }
        if (travelTrendsWidget != null) {
            getContentPane().setComponentZOrder(travelTrendsWidget, 1);
        }
        if (recentSearchesWidget != null) {
            getContentPane().setComponentZOrder(recentSearchesWidget, 2);
        }
    }

    if (centerContentDeck != null) {
        for (Component comp : centerContentDeck.getComponents()) {
            if ("bookingViewCard".equals(comp.getName())) {
                comp.setBounds(0, 0, centerColumnW, canvasH - 75);
            }
        }
    }

    sidebarPanel.revalidate();
    sidebarPanel.repaint();

    getContentPane().revalidate();
    getContentPane().repaint();
}
        });
    }

   private void switchActiveScreen(JButton clickedButton, String cardLookUpKey) {
    JButton[] menuButtons = {dashboardBtn, bookingsBtn, exploreBtn, hotelsBtn, guidesBtn, gallaryBtn, settingsBtn};
    for (JButton btn : menuButtons) {
        if (btn != null) {
            btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            btn.setName("");
        }
    }
    applyActiveMenuState(clickedButton);
    
    boolean isDashboard = cardLookUpKey.equals("DASHBOARD");
    if (travelTrendsWidget != null) travelTrendsWidget.setVisible(isDashboard);
    if (recentSearchesWidget != null) recentSearchesWidget.setVisible(isDashboard);
    if (centralizedSearchField != null) {
        switch (cardLookUpKey) {
            case "EXPLORE" -> {
                centralizedSearchField.setVisible(true);
                centralizedSearchField.setText("Search Destinations, Hotels, & Experiences...");
                centralizedSearchField.setForeground(new Color(220, 220, 220));
            }
            case "HOTELS" -> {
                centralizedSearchField.setVisible(true);
                centralizedSearchField.setText("Search luxury hotels, resorts, and villas...");
                centralizedSearchField.setForeground(new Color(220, 220, 220));
            }
            case "DASHBOARD" -> {
                centralizedSearchField.setVisible(true);
                centralizedSearchField.setText("Search Destinations, Hotels, & Experiences...");
                centralizedSearchField.setForeground(new Color(220, 220, 220));
            }
            default -> 
                centralizedSearchField.setVisible(false);
        }
    }
    contentDeckLayout.show(centerContentDeck, cardLookUpKey);
    
    // 🌟 FORCE LAYER Z-ORDER REFRESH PASS: 
    // This stops CardLayout from swallowing or burying the search field when you switch back!
    if (centralizedSearchField != null && centralizedSearchField.isVisible()) {
        getContentPane().setComponentZOrder(centralizedSearchField, 0);
    }
    
    if (this.getComponentListeners().length > 0) {
        ComponentListener cl = this.getComponentListeners()[0];
        cl.componentResized(new ComponentEvent(this, ComponentEvent.COMPONENT_RESIZED));
    }
    
    getContentPane().repaint();
}

    private JButton createGlassMenuButton(String text, String iconPath) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getBorder() != null && getBorder().toString().contains("active")) {
                    g2.setColor(new Color(0, 200, 255, 50)); 
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    g2.setColor(new Color(0, 200, 255, 150));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 25)); 
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        try {
            URL url = getClass().getResource(iconPath);
            if (url != null) {
                Image scaled = new ImageIcon(url).getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(scaled));
                btn.setIconTextGap(12);
            }
        } catch(Exception e) {}

        return btn;
    }

    private void applyActiveMenuState(JButton button) {
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 200, 255), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setName("active");
    }

    private JPanel createDestinationCard(String city, String details, float rating, String imagePath){
        final Image[] cardImage = {null};
        try {
            URL imgURL = getClass().getResource(imagePath);
            if (imgURL != null){
                cardImage[0] = new ImageIcon(imgURL).getImage();
            }
        } catch (Exception e){
            System.out.println("Could not load card image: " + imagePath);
        }

        JPanel card = new JPanel(){
            private boolean isHovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        repaint();
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isHovered) {
                    g2.setColor(new Color(255, 255, 255, 55));
                } else {
                    g2.setColor(new Color(255, 255, 255, 30));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                if (cardImage[0] != null) {
                    g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), 100, 20, 20));
                    g2.drawImage(cardImage[0], 0, 0, getWidth(), 100, this);
                    g2.setClip(null);
                } else {
                    g2.setColor(new Color(0, 0, 0, 40));
                    g2.fillRoundRect(0, 0, getWidth(), 100, 20, 20);
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.drawString("🏨 Preview Image Unavailable", 20, 55);
                }
                
                // Add animated layout borders highlighting components under hover selection rules
                if (isHovered) {
                    g2.setColor(new Color(0, 200, 255, 150));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                }
                g2.dispose();
            }
        };
        card.setLayout(null);
        card.setOpaque(false);
        
        JLabel name = new JLabel(city);
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));
        name.setForeground(Color.WHITE);
        name.setBounds(15, 112, 180, 20);
        card.add(name);
        
        JLabel desc = new JLabel(details);
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        desc.setForeground(new Color(210, 210, 210));
        desc.setBounds(15, 134, 180, 20);
        card.add(desc);

        JLabel stars = new JLabel("⭐ " + rating);
        stars.setFont(new Font("Segoe UI", Font.BOLD, 11));
        stars.setForeground(new Color(255, 210, 50));
        stars.setBounds(15, 160, 80, 20);
        card.add(stars);
        
        return card;
    }

    private void initBookingsViewComponent() {
    bookingsViewPanel = createBlankGlassViewScreen("Your Itinerary Ledger");
    bookingsViewPanel.setLayout(null);
    
    JLabel activeHeader = new JLabel("Current & Upcoming Reservations");
    activeHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
    activeHeader.setForeground(new Color(0, 200, 255));
    activeHeader.setBounds(40, 75, 400, 25);
    bookingsViewPanel.add(activeHeader);
    
    JPanel tableHeader = new JPanel(null);
    tableHeader.setOpaque(false);
    tableHeader.setBounds(40, 110, 760, 35);
    
    String[] headers = {"Destination / Hotel", "Booking Reference", "Scheduled Date", "Status & Price"};
    int[] colWidths = {240, 180, 150, 180}; 
    int[] colX = {5, 250, 440, 600};  
    
    for (int i = 0; i < 4; i++) {
        JLabel hLabel = new JLabel(headers[i]);
        hLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        hLabel.setForeground(new Color(0, 195, 255));
        hLabel.setBounds(colX[i], 5, colWidths[i], 25);
        if (i > 1) hLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tableHeader.add(hLabel);
    }
    bookingsViewPanel.add(tableHeader);

    JPanel dataGrid = new JPanel(null);
    dataGrid.setOpaque(false);
    
    String dbUrl = "jdbc:mysql://localhost:3306/travelsphere_db";
    String query = "SELECT booking_id, destination, booking_type, booking_date, price, status FROM bookings WHERE user_id = ? ORDER BY created_at DESC";
    int index = 0;

    try (java.sql.Connection conn = java.sql.DriverManager.getConnection(dbUrl, "root", "");
         java.sql.PreparedStatement pstmt = conn.prepareStatement(query)) {
        
        pstmt.setInt(1, loggedInUserId);
        java.sql.ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            final int currentBookingId = rs.getInt("booking_id");
            final String dest = rs.getString("destination");
            final String ref = "TS-REF-" + currentBookingId;
            final String date = rs.getString("booking_date");
            final double rawPrice = rs.getDouble("price");
            final String rawStatus = rs.getString("status");
            final String stat = rawStatus + " ($" + rawPrice + ")";

            // Create the Interactive Row Panel
            JPanel row = new JPanel(null) {
                private boolean isHovered = false;
                {
                    // Track row hover behavior animations
                    addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseEntered(java.awt.event.MouseEvent e) {
                            isHovered = true;
                            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                            repaint();
                        }
                        @Override
                        public void mouseExited(java.awt.event.MouseEvent e) {
                            isHovered = false;
                            repaint();
                        }
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent e) {
                            // Fetch parent Layered Glass Pane window hierarchy safely
                            JLayeredPane glassPane = (JLayeredPane) javax.swing.SwingUtilities.getAncestorOfClass(JLayeredPane.class, bookingsViewPanel);
                            if (glassPane != null) {
                                showBookingDetailsModal(glassPane, currentBookingId, dest, ref, date, rawStatus, rawPrice);
                            }
                        }
                    });
                }

                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    // Changes tint transparency smoothly if mouse passes over this row
                    g2.setColor(isHovered ? new Color(255, 255, 255, 28) : new Color(255, 255, 255, 12));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    
                    if (isHovered) {
                        g2.setColor(new Color(0, 195, 255, 60));
                        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                    }
                    g2.dispose();
                }
            };
            row.setOpaque(false);
            row.setBounds(0, index * 50, 760, 45);

            String[] rowData = {dest, ref, date, stat};
            for (int i = 0; i < 4; i++) {
                JLabel dLabel = new JLabel(rowData[i]);
                dLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                dLabel.setForeground(new Color(220, 230, 245));
                dLabel.setBounds(colX[i], 10, colWidths[i], 25);
                if (i > 1) dLabel.setHorizontalAlignment(SwingConstants.CENTER);
                row.add(dLabel);
            }
            
            dataGrid.add(row);
            index++;
        }
    } catch (Exception e) {
        System.out.println("Error loading bookings: " + e.getMessage());
    }

    dataGrid.setPreferredSize(new Dimension(740, index * 50));
    JScrollPane scroll = new JScrollPane(dataGrid);
    scroll.setOpaque(false);
    scroll.getViewport().setOpaque(false);
    scroll.setBorder(null);
    scroll.setBounds(40, 150, 760, 350);
    
    bookingsViewPanel.add(scroll);
}
    private void showBookingDetailsModal(JLayeredPane glassPane, int bookingId, String destination, String ref, String date, String status, double price) {
    // 1. Dark frosted window blocker panel overlay mask background
    JPanel modalMask = new JPanel(null) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(10, 15, 28, 190));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        }
    };
    modalMask.setOpaque(false);
    modalMask.setBounds(0, 0, glassPane.getWidth(), glassPane.getHeight());

    // 2. Main details dialog panel popup box dimension layouts
    int cardW = 440;
    int cardH = 380;
    int cardX = (glassPane.getWidth() - cardW) / 2;
    int cardY = (glassPane.getHeight() - cardH) / 2;

    JPanel card = new JPanel(null) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(22, 32, 54, 250)); // Deep premium slate blue panel styling
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
            g2.setColor(new Color(255, 255, 255, 35));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 24, 24);
            g2.dispose();
        }
    };
    card.setOpaque(false);
    card.setBounds(cardX, cardY, cardW, cardH);

    // Header label string
    JLabel header = new JLabel("Reservation Details");
    header.setFont(new Font("Segoe UI", Font.BOLD, 18));
    header.setForeground(Color.WHITE);
    header.setBounds(35, 25, 250, 30);
    card.add(header);

    // Upper right corner window cancellation close control label button
    JLabel closeX = new JLabel("X", SwingConstants.CENTER);
    closeX.setFont(new Font("Segoe UI", Font.BOLD, 20));
    closeX.setForeground(new Color(239, 68, 68));
    closeX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    closeX.setBounds(cardW - 50, 25, 25, 25);
    closeX.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            glassPane.remove(modalMask);
            glassPane.revalidate();
            glassPane.repaint();
        }
        @Override
        public void mouseEntered(java.awt.event.MouseEvent e) { closeX.setForeground(Color.WHITE); }
        @Override
        public void mouseExited(java.awt.event.MouseEvent e) { closeX.setForeground(new Color(239, 68, 68)); }
    });
    card.add(closeX);

    // 3. Render metadata detail string labels rows stack mapping
    String[] titles = {"Target Destination:", "Booking Reference:", "Itinerary Date:", "Reservation Status:", "Total Pricing Cost:"};
    String[] values = {destination, ref, date, status.toUpperCase(), "$" + price};
    int currentY = 80;

    for (int i = 0; i < titles.length; i++) {
        JLabel titleLbl = new JLabel(titles[i]);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLbl.setForeground(new Color(0, 195, 255));
        titleLbl.setBounds(35, currentY, 150, 25);
        card.add(titleLbl);

        JLabel valueLbl = new JLabel(values[i]);
        valueLbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        valueLbl.setForeground(Color.WHITE);
        valueLbl.setBounds(190, currentY, 220, 25);
        card.add(valueLbl);

        currentY += 36;
    }

    // 4. Cancel Booking Action Button Implementation
    JButton cancelActionBtn = new JButton("Cancel This Reservation") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isPressed() ? new Color(150, 20, 20) : 
                     getModel().isRollover() ? new Color(190, 30, 30) : new Color(225, 40, 40));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.dispose();
            super.paintComponent(g);
        }
    };
    cancelActionBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
    cancelActionBtn.setForeground(Color.WHITE);
    cancelActionBtn.setContentAreaFilled(false);
    cancelActionBtn.setBorderPainted(false);
    cancelActionBtn.setFocusPainted(false);
    cancelActionBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    cancelActionBtn.setBounds(35, cardH - 75, cardW - 70, 42);

    cancelActionBtn.addActionListener(e -> {
        int confirm = JOptionPane.showConfirmDialog(glassPane, 
                "Are you absolutely sure you want to drop booking reference: " + ref + "?",
                "Confirm Cancellation Request", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Trigger MySQL Database record removal pass sequence routine
            executeRecordCancellation(bookingId);
            
            // Drop modal dialog workspace panel layer masking
            glassPane.remove(modalMask);
            
            // Refresh itineraries grid layout view window screen right away
            initBookingsViewComponent();
            
            glassPane.revalidate();
            glassPane.repaint();
        }
    });

    card.add(cancelActionBtn);
    modalMask.add(card);
    
    // Add inside glasspane overlay structure layer contexts cleanly
    glassPane.add(modalMask, JLayeredPane.MODAL_LAYER);
    glassPane.setComponentZOrder(modalMask, 0);
    glassPane.revalidate();
    glassPane.repaint();
}
    private void executeRecordCancellation(int bookingId) {
    String dbUrl = "jdbc:mysql://localhost:3306/travelsphere_db";
    String sql = "DELETE FROM bookings WHERE booking_id = ?";

    try (java.sql.Connection conn = java.sql.DriverManager.getConnection(dbUrl, "root", "");
         java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, bookingId);
        int feedbackResult = pstmt.executeUpdate();
        
        if (feedbackResult > 0) {
            JOptionPane.showMessageDialog(null, 
                "The reservation has been cancelled. Your payment has been voided.", 
                "Booking Dropped Successfully", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (Exception ex) {
        System.out.println("SQL execution error processing cancellation drop request: " + ex.getMessage());
        JOptionPane.showMessageDialog(null, 
            "Error updating database ledger record: " + ex.getMessage(), 
            "Database Connectivity Failure", JOptionPane.ERROR_MESSAGE);
    }
}
private void inithotelsViewPanel(){
    hotelsViewPanel = createBlankGlassViewScreen("Explore Premium Accommodations & Stays");
    hotelsViewPanel.setLayout(null);
    hotelsGrid = new JPanel();
    hotelsGrid.setOpaque(false);
    hotelsGrid.setLayout(null);

    loadHotelsFromDatabase(hotelsGrid, "");

    hotelsScrollPane = new JScrollPane(hotelsGrid);
    hotelsScrollPane.setOpaque(false);
    hotelsScrollPane.getViewport().setOpaque(false);
    hotelsScrollPane.setBorder(null);
    hotelsScrollPane.setViewportBorder(null);
    hotelsScrollPane.getVerticalScrollBar().setUnitIncrement(16);
    hotelsScrollPane.setBounds(25, 170, 550, 350);
    hotelsViewPanel.add(hotelsScrollPane);
}
    private void initGuidesViewComponent() {
        guidesViewPanel = createBlankGlassViewScreen("Curated Travel Knowledge Guides");
        
        JPanel guidesContainer = new JPanel();
        guidesContainer.setOpaque(false);
        guidesContainer.setLayout(null);
        guidesContainer.setBounds(20, 80, 550, 400);
        
        JPanel guide1 = createListIndicatorCard("📚 Ultimate Packing List for Mediterranean Cruises", "Read Time: 5 mins • Updated recently");
        guide1.setBounds(0, 0, 530, 60);
        guidesContainer.add(guide1);
        
        JPanel guide2 = createListIndicatorCard("⛩️ Navigating Kyoto's Subway System: A Local's Guide", "Read Time: 8 mins • Includes map attachments");
        guide2.setBounds(0, 70, 530, 60);
        guidesContainer.add(guide2);
        
        JPanel guide3 = createListIndicatorCard("💳 Traveling Smart: Avoiding International Card Fees", "Read Time: 4 mins • Financial safety rules");
        guide3.setBounds(0, 140, 530, 60);
        guidesContainer.add(guide3);
        
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
        
        int contentWidth = 600; 
        int panelWidth = 800;
        int midX = (panelWidth - contentWidth) / 2; 
        
        int avatarSize = 100;
        int avatarX = midX + (contentWidth - avatarSize) / 2;
        JPanel avatarCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 200, 255));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(40, 50, 70));
                g2.fillOval(4, 4, getWidth() - 8, getHeight() - 8);
                g2.dispose();
            }
        };
        avatarCircle.setLayout(null);
        avatarCircle.setBounds(avatarX, 80, avatarSize, avatarSize);
        
        JLabel avatarText = new JLabel("I", SwingConstants.CENTER);
        avatarText.setFont(new Font("Segoe UI", Font.BOLD, 42));
        avatarText.setForeground(Color.WHITE);
        avatarText.setBounds(0, 0, avatarSize, avatarSize);
        avatarCircle.add(avatarText);
        profileViewPanel.add(avatarCircle);
        
        JLabel usernameLabel = new JLabel("Imran Hammad", SwingConstants.CENTER);
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(midX, 195, contentWidth, 30);
        profileViewPanel.add(usernameLabel);
        
        JLabel tierLabel = new JLabel("👑 Explorer", SwingConstants.CENTER);
        tierLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tierLabel.setForeground(new Color(0, 200, 255));
        tierLabel.setBounds(midX, 225, contentWidth, 20);
        profileViewPanel.add(tierLabel);
        loadUserProfileData(usernameLabel, tierLabel, avatarText);
     
        int btnW1 = 160, btnW2 = 210, btnPadding = 15;
        int combinedButtonsWidth = btnW1 + btnPadding + btnW2;
        int buttonsStartX = midX + (contentWidth - combinedButtonsWidth) / 2;
        JButton editProfileBtn = createStyledProfileButton("📝 Edit Profile Settings", new Color(255, 255, 255, 30), Color.WHITE);
        editProfileBtn.setBounds(buttonsStartX, 260, btnW1, 35);
        editProfileBtn.setFocusable(true);
        for (java.awt.event.ActionListener al : editProfileBtn.getActionListeners()) {
            editProfileBtn.removeActionListener(al);
        }
        editProfileBtn.addActionListener((java.awt.event.ActionEvent e) -> {
            JButton srcBtn = (JButton) e.getSource();
            JPanel parentPanel = (JPanel) srcBtn.getParent();
            if (parentPanel == null) return;
            for (Component c : parentPanel.getComponents()) {
                if ("EDIT_OVERLAY".equals(c.getName())) return;
            }
            JPanel modalBackground = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(10, 15, 30, 225));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.dispose();
                }
            };
            modalBackground.setLayout(null);
            modalBackground.setName("EDIT_OVERLAY");
            modalBackground.setBounds(0, 0, parentPanel.getWidth(), parentPanel.getHeight());
            JPanel dialogCard = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(255, 255, 255, 22));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.setColor(new Color(255, 255, 255, 40));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                    g2.dispose();
                }
            };
            dialogCard.setLayout(null);
            dialogCard.setOpaque(false);
            int dialogW = 380, dialogH = 340;
            dialogCard.setBounds((modalBackground.getWidth() - dialogW) / 2, 60, dialogW, dialogH);
            modalBackground.add(dialogCard);
            JLabel modalTitle = new JLabel("Modify Profile Parameters", SwingConstants.CENTER);
            modalTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
            modalTitle.setForeground(Color.WHITE);
            modalTitle.setBounds(0, 15, dialogW, 25);
            dialogCard.add(modalTitle);
            JLabel nameEditLbl = new JLabel("Update Account Name:");
            nameEditLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            nameEditLbl.setForeground(new Color(180, 180, 180));
            nameEditLbl.setBounds(30, 55, 200, 15);
            dialogCard.add(nameEditLbl);
            JTextField editNameField = new JTextField(usernameLabel != null ? usernameLabel.getText() : "");
            setupGlassTextFieldStyle(editNameField);
            editNameField.setBounds(30, 75, dialogW - 60, 35);
            dialogCard.add(editNameField);
            JLabel bioEditLbl = new JLabel("Update Explorer Bio Status:");
            bioEditLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            bioEditLbl.setForeground(new Color(180, 180, 180));
            bioEditLbl.setBounds(30, 125, 200, 15);
            dialogCard.add(bioEditLbl);
            JTextField editBioField = new JTextField(tierLabel != null ? tierLabel.getText().replace("👑 ", "") : "");
            setupGlassTextFieldStyle(editBioField);
            editBioField.setBounds(30, 145, dialogW - 60, 35);
            dialogCard.add(editBioField);
            JButton cancelBtn = createStyledProfileButton("Cancel", new Color(255, 70, 70, 40), new Color(255, 120, 120));
            cancelBtn.setBounds(30, 265, 150, 40);
            dialogCard.add(cancelBtn);
            JButton saveBtn = createStyledProfileButton("Save Changes", new Color(0, 200, 255, 50), Color.WHITE);
            saveBtn.setBounds(dialogW - 180, 265, 150, 40);
            dialogCard.add(saveBtn);
            cancelBtn.addActionListener(cancelEvt -> {
                parentPanel.remove(modalBackground);
                parentPanel.revalidate();parentPanel.repaint();
            });
            saveBtn.addActionListener(saveEvt -> {
                String inputNameText = editNameField.getText().trim();
                String inputBioText = editBioField.getText().trim();
                
                if (!inputNameText.isEmpty()) {
                    if (usernameLabel != null) usernameLabel.setText(inputNameText);
                    if (avatarText != null) {
                        avatarText.setText(inputNameText.substring(0, 1).toUpperCase());
                    }
                    
                    // Updates the data matching the unique user session ID
                    String updateQuery = "UPDATE users SET username = ?, bio = ? WHERE user_id = ?";
                    String dbUrl = "jdbc:mysql://localhost:3306/travelsphere_db";
                    String dbUser = "root";
                    String dbPass = "";

                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        try (java.sql.Connection conn = java.sql.DriverManager.getConnection(dbUrl, dbUser, dbPass);
                             java.sql.PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                            
                            pstmt.setString(1, inputNameText);
                            pstmt.setString(2, inputBioText);
                            pstmt.setInt(3, loggedInUserId); 
                            
                            pstmt.executeUpdate();
                        }
                    } catch (ClassNotFoundException | SQLException ex) {
                        System.out.println("Database save execution error: " + ex.getMessage());
                    }
                }
                if (!inputBioText.isEmpty() && tierLabel != null) {
                    tierLabel.setText("👑 " + inputBioText);
                }
                
                parentPanel.remove(modalBackground); 
                parentPanel.revalidate();parentPanel.repaint();
            });
            parentPanel.add(modalBackground);
            parentPanel.setComponentZOrder(modalBackground, 0);
            parentPanel.revalidate();
            parentPanel.repaint();
        });

        JButton innerLogoutBtn = createStyledProfileButton("🚪 Terminate Session & Logout", new Color(255, 70, 70, 40), new Color(255, 100, 100));
        innerLogoutBtn.setBounds(buttonsStartX + btnW1 + btnPadding, 260, btnW2, 35);
        innerLogoutBtn.setFocusable(true);
        for (java.awt.event.ActionListener al : innerLogoutBtn.getActionListeners()) {
            innerLogoutBtn.removeActionListener(al);
        }
        innerLogoutBtn.addActionListener((var e) -> {
            System.out.println("[Session Control] Executing isolated window sequence...");
            java.awt.Window currentWindow = javax.swing.SwingUtilities.getWindowAncestor((Component) e.getSource());
            if (currentWindow != null) {
                currentWindow.dispose();
            }
            try {
                Class<?> loginClazz = Class.forName("com.travelsphere.gui.LoginFrame");
                JFrame loginFrame = (JFrame) loginClazz.getDeclaredConstructor().newInstance();
                loginFrame.setVisible(true);
            } catch(ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException ex) {
                System.out.println("LoginFrame could not be resolved structurally via routing context.");
            }
        });

        if (profileViewPanel != null) {
            profileViewPanel.remove(editProfileBtn);
            profileViewPanel.remove(innerLogoutBtn);
            
            profileViewPanel.add(editProfileBtn);
            profileViewPanel.add(innerLogoutBtn);
            profileViewPanel.revalidate();profileViewPanel.repaint();
        }

        profileViewPanel.setComponentZOrder(editProfileBtn, 0);
        profileViewPanel.setComponentZOrder(innerLogoutBtn, 0);

        JLabel rewardsHeader = new JLabel("🏆 Your Explorer Milestone Achievements", SwingConstants.CENTER);
        rewardsHeader.setFont(new Font("Segoe UI", Font.BOLD, 14));
        rewardsHeader.setForeground(Color.WHITE);
        rewardsHeader.setBounds(midX, 330, contentWidth, 25);
        profileViewPanel.add(rewardsHeader);
        
        int badgeW = 180;
        int badgeH = 75;
        int badgeGap = 15;
        int totalBadgesWidth = (badgeW * 3) + (badgeGap * 2);
        int badgeStartX = midX + (contentWidth - totalBadgesWidth) / 2;
        
        JPanel badge1 = createMilestoneBadgeCard("? Wanderlust Status", "6 Countries Logged");
        badge1.setBounds(badgeStartX, 370, badgeW, badgeH);
        profileViewPanel.add(badge1);
        
        JPanel badge2 = createMilestoneBadgeCard("✈️ Jetsetter Logs", "48,200 Total Air Miles");
        badge2.setBounds(badgeStartX + badgeW + badgeGap, 370, badgeW, badgeH);
        profileViewPanel.add(badge2);
        
        JPanel badge3 = createMilestoneBadgeCard("⭐ Elite Reviewer", "12 Hotel Submissions");
        badge3.setBounds(badgeStartX + (badgeW + badgeGap) * 2, 370, badgeW, badgeH);
        profileViewPanel.add(badge3);
    }
    private JPanel createBlankGlassViewScreen(String panelTitle){
        JPanel view = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillRoundRect(10, 10, getWidth() - 20, getHeight() - 20, 25, 25);
                g2.dispose();
            }
        };
        view.setLayout(null);
        view.setOpaque(false);

        JLabel label = new JLabel(panelTitle);
        label.setFont(new Font("Segoe UI", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        label.setBounds(20, 25, 400, 30);
        view.add(label);
        return view;
    }
    private JPanel createListIndicatorCard(String cardTitle, String cardSubtitle){
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 20));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(new Color(255, 255, 255, 35));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
            }
        };
        panel.setLayout(null);
        panel.setOpaque(false);
        
        JLabel title = new JLabel(cardTitle);
        title.setFont(new Font("Segoe UI", Font.BOLD, 13));
        title.setForeground(Color.WHITE);
        title.setBounds(15, 10, 450, 20);
        panel.add(title);
        
        JLabel subtitle = new JLabel(cardSubtitle);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subtitle.setForeground(new Color(180, 180, 180));
        subtitle.setBounds(15, 30, 450, 20);
        panel.add(subtitle);
        
        return panel;
    }
private JButton createStyledProfileButton(String text, Color bg, Color fg){
        JButton button = new JButton(text) {
            private boolean isHovered = false;
            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) { isHovered = true; repaint(); }
                    @Override
                    public void mouseExited(MouseEvent e) { isHovered = false; repaint(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isHovered) {
                    g2.setColor(bg.brighter());
                } else {
                    g2.setColor(bg);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(fg);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

private JPanel createMilestoneBadgeCard(String header, String value) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 12));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(new Color(255, 255, 255, 20));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
            }
        };
        card.setLayout(null);
        card.setOpaque(false);
        
        JLabel headLbl = new JLabel(header, SwingConstants.CENTER);
        headLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        headLbl.setForeground(new Color(180, 180, 180));
        headLbl.setBounds(0, 15, 180, 15); 
        card.add(headLbl);
        
        JLabel valLbl = new JLabel(value, SwingConstants.CENTER);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valLbl.setForeground(new Color(0, 200, 255));
        valLbl.setBounds(0, 38, 180, 25);
        card.add(valLbl);
        
        return card;
    }
private void setupGlassTextFieldStyle(JTextField field) {
        field.setOpaque(false);
        field.setBackground(new Color(255, 255, 255, 15)); 
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE); 
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 40), 2, true), 
            BorderFactory.createEmptyBorder(0, 12, 0, 12) 
        ));
    }

private void openItemDetailPanel(String itemTitle, int itemId, boolean isHotel) {

    Component[] components = centerContentDeck.getComponents();
    for (Component comp : components) {
        if (comp instanceof ItemDetailPanel) {
            centerContentDeck.remove(comp);
        }
    }
    

    ItemDetailPanel detailsView = new ItemDetailPanel(this, centerContentDeck, contentDeckLayout, itemTitle, itemId, isHotel);
    

    centerContentDeck.add(detailsView, "detailView");
    contentDeckLayout.show(centerContentDeck, "detailView");

    centerContentDeck.revalidate();
    centerContentDeck.repaint();
}
private void loadDestinationsFromDatabase(JPanel targetGridPanel, int rowLimit, String filterText) {
    targetGridPanel.removeAll();
    String query = "SELECT destination_id, city, description, rating, image_path FROM destinations";
    boolean hasFilter = (filterText != null && !filterText.trim().isEmpty() && !filterText.startsWith("Search"));
    if (hasFilter) {
        query += " WHERE city LIKE ? OR description LIKE ?";
    }
    query += " LIMIT " + rowLimit;

    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        
        if (hasFilter) {
            String wildcard = "%" + filterText.trim() + "%";
            pstmt.setString(1, wildcard);
            pstmt.setString(2, wildcard);
        }
        
        ResultSet rs = pstmt.executeQuery();
        boolean recordsFound = false;
        while (rs.next()) {
            recordsFound = true;
            
            // 🌟 STAMPED CHANGE: Extract the destination primary key ID from the row!
            int destId = rs.getInt("destination_id"); 
            
            String city = rs.getString("city");
            String desc = rs.getString("description");
            float rating = rs.getFloat("rating");
            String imgPath = rs.getString("image_path");
            
            // Keep your card visual component generation exactly the same
            JPanel destinationCard = createDestinationCard(city, desc, rating, imgPath);
            destinationCard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            destinationCard.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    // 🌟 STAMPED CHANGE: Route to our custom detail layout class using your dashboard frame fields!
                    openItemDetailPanel(city, destId, false); 
                }
            });
            targetGridPanel.add(destinationCard);
        }
        
        if (!recordsFound) {
            injectFilteredFallbackDestinations(targetGridPanel, filterText);
        } else {
            targetGridPanel.revalidate();
            targetGridPanel.repaint();
        }
    } catch (Exception ex) {
        injectFilteredFallbackDestinations(targetGridPanel, filterText);
    }
}
private void loadHotelsFromDatabase(JPanel targetGridPanel, String filterText){
    targetGridPanel.removeAll();
    int index = 0;
    int cardWidth = 240;
    int cardHeight = 190;
    int gapX = 25;
    int gapY = 20;
    
    // 🌟 STAMPED CHANGE: Included hotel_id in the SELECT string query statement
    String query = "SELECT hotel_id, hotel_name, price_per_night, star_rating, amenities, hotel_image FROM hotels";
    boolean hasFilter = (filterText != null && !filterText.trim().isEmpty() && !filterText.startsWith("Search"));
    
    if(hasFilter){
        query += " WHERE hotel_name LIKE ? OR destination_id IN (SELECT destination_id FROM destinations WHERE city LIKE ?)";
    }

    try(Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
         PreparedStatement pstmt = conn.prepareStatement(query)){
        
        if(hasFilter){
            String wildcard = "%" + filterText.trim() + "%";
            pstmt.setString(1, wildcard);
            pstmt.setString(2, wildcard);
        }
        
        ResultSet rs = pstmt.executeQuery();
        boolean hasData = false;
        while (rs.next()) {
            hasData = true;
            
            // 🌟 STAMPED CHANGE: Extract the hotel primary key ID from the row matching your phpMyAdmin setup
            int hotelId = rs.getInt("hotel_id");
            
            String name = rs.getString("hotel_name");
            double price = rs.getDouble("price_per_night");
            int rating = rs.getInt("star_rating");
            String imgPath = rs.getString("hotel_image");
            
            String subtext = "Price: $" + price + "/night";
            JPanel card = createDestinationCard(name, subtext, (float) rating, imgPath);
            card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            card.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    // 🌟 STAMPED CHANGE: Divert routing to openItemDetailPanel instead of old booking template
                    openItemDetailPanel(name, hotelId, true);
                }
            });
            
            int x = (index % 2) * (cardWidth + gapX);
            int y = (index / 2) * (cardHeight + gapY);
            card.setBounds(x, y, cardWidth, cardHeight);
            
            targetGridPanel.add(card);
            index++;
        }
        
        if (hasData) {
            int totalRows = (int) Math.ceil((double) index / 2);
            targetGridPanel.setPreferredSize(new Dimension(530, totalRows * (cardHeight + gapY) + 20));
            targetGridPanel.revalidate();
            targetGridPanel.repaint();
            return;
        }
    } catch (Exception ex) {
        System.out.println("Database offline: " + ex.getMessage());
    }
    
    injectFallbackHotels(targetGridPanel, filterText);
}

private void loadUserProfileData(JLabel usernameLabel, JLabel tierLabel, JLabel avatarText) {
    String query = "SELECT username, bio FROM users WHERE user_id = ? LIMIT 1";
    String dbUrl = "jdbc:mysql://localhost:3306/travelsphere_db";
    String dbUser = "root";
    String dbPass = "";
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, loggedInUserId); 
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String dbName = rs.getString("username");
                String dbBio = rs.getString("bio");
                
                if (dbName != null && !dbName.trim().isEmpty()) {
                    usernameLabel.setText(dbName);
                    if (avatarText != null) {
                        avatarText.setText(dbName.substring(0, 1).toUpperCase());
                    }
                }
                if (dbBio != null && !dbBio.trim().isEmpty()) {
                    tierLabel.setText("👑 " + dbBio);
                }
            }
        }
    } catch (ClassNotFoundException | SQLException ex) {
        System.out.println("Database load error: " + ex.getMessage());
    }
}
/**
 * Automatically queries the database to extract live metrics from the bookings table,
 * sorting and displaying the top 5 trending items inside the absolute list container.
 */
private void loadTravelTrendsData(JPanel listContainer) {
    // Clear out previous components to rebuild cleanly on refreshes
    listContainer.removeAll(); 

    // Automated SQL pass — updated to extract the top 5 rows
    String sql = "SELECT destination, COUNT(*) as interaction_count " +
                 "FROM bookings " + 
                 "GROUP BY destination " + 
                 "ORDER BY interaction_count DESC LIMIT 3";

    try (Connection conn = com.travelsphere.database.DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        java.util.List<String> destinationsList = new java.util.ArrayList<>();
        java.util.List<Integer> countsList = new java.util.ArrayList<>();
        int maxCount = 0;

        while (rs.next()) {
            String name = rs.getString("destination");
            int count = rs.getInt("interaction_count");
            
            destinationsList.add(name);
            countsList.add(count);
            if (count > maxCount) maxCount = count; 
        }

        // Render Trend Cards dynamically into the container list
        if (destinationsList.isEmpty()) {
            JLabel emptyLabel = new JLabel("No active booking data recorded yet.");
            emptyLabel.setForeground(Color.LIGHT_GRAY);
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
            listContainer.add(emptyLabel);
        } else {
            for (int i = 0; i < destinationsList.size(); i++) {
                String destinationName = destinationsList.get(i);
                int rawCount = countsList.get(i);
                
                int visualPercentage = (maxCount > 0) ? (int) ((rawCount / (double) maxCount) * 100) : 0;
                String descriptiveMetricText = rawCount + (rawCount == 1 ? " booking" : " bookings");

                // Clean truncation so names don't expand past the widget column borders
                if (destinationName.length() > 22) {
                    destinationName = destinationName.substring(0, 19) + "...";
                }

                // Pass a quick Lambda function down to the cards so clicking a row re-triggers this exact loader method!
                TrendRowItem trendCard = new TrendRowItem(destinationName, visualPercentage, descriptiveMetricText, () -> {
                    loadTravelTrendsData(listContainer);
                });
                
                listContainer.add(trendCard);
                listContainer.add(Box.createVerticalStrut(4)); 
            }
        }
    } catch (SQLException e) {
        System.out.println("Error pulling dynamic top 5 trends: " + e.getMessage());
        JLabel errorLabel = new JLabel("Failed to sync live data.");
        errorLabel.setForeground(new Color(239, 68, 68));
        errorLabel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        listContainer.add(errorLabel);
    }

    // Force Swing to recalculate layouts and repaint the panel structure instantly
    listContainer.revalidate();
    listContainer.repaint();
}
/**
 * Logs a search query to the database. If the query already exists, it removes 
 * the old record first so only the most recent unique instance is kept.
     * @param query
 */
public void logRecentSearch(String query) {
    if (query == null || query.trim().isEmpty()) return;

    String cleanQuery = query.trim().toLowerCase(); // Keep it lowercase to match your image logic perfectly

    // 1. Delete the old entry if it already exists anywhere in the table
    String deleteSql = "DELETE FROM recent_searches WHERE LOWER(search_query) = ?";
    // 2. Insert the new instance (which automatically gets the newest timestamp)
    String insertSql = "INSERT INTO recent_searches (search_query) VALUES (?)";
    
    try (Connection conn = com.travelsphere.database.DatabaseConnection.getConnection()) {
        
        // Ensure auto-commit is on so it writes permanently to disk immediately
        conn.setAutoCommit(true); 
        
        // Step A: Run the delete cleaner pass
        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
            deleteStmt.setString(1, cleanQuery);
            deleteStmt.executeUpdate();
        }
        
        // Step B: Insert the fresh record
        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            insertStmt.setString(1, cleanQuery);
            insertStmt.executeUpdate();
            System.out.println("Cleanly persisted unique search query: " + cleanQuery);
        }
        
        // Step C: Instantly refresh your beautiful carousel card graphics
        loadRecentSearchesData();
        
    } catch (SQLException e) {
        System.out.println("Error maintaining unique search history: " + e.getMessage());
    }
}

private Timer carouselTimer = null; // Store globally in DashboardFram to prevent duplicate loops

/**
 * Renders the recent searches database queries into a sleek carousel slider card.
 * Features unified layered rendering to prevent absolute component clipping layout bugs.
 */
private void loadRecentSearchesData() {
    if (recentSearchesWidget == null) return;
    
    // Stop any dangling carousel timers to eliminate duplicate threading bugs
    if (carouselTimer != null) {
        carouselTimer.stop();
    }
    
    // Clear out everything entirely to make room for a fresh paint pass
    recentSearchesWidget.removeAll();
    recentSearchesWidget.setLayout(null);

    // Explicitly force the widget boundaries right out of the gate!
    int widgetWidth = 270;
    int widgetHeight = 185;
    recentSearchesWidget.setSize(widgetWidth, widgetHeight);

    // 1. Pull up to 3 unique latest items out of phpMyAdmin
    java.util.List<String> userQueries = new java.util.ArrayList<>();
    String sql = "SELECT search_query, MAX(search_timestamp) FROM recent_searches " +
                 "GROUP BY search_query ORDER BY MAX(search_timestamp) DESC LIMIT 3";

    try (Connection conn = com.travelsphere.database.DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
            userQueries.add(rs.getString("search_query"));
        }
    } catch (SQLException e) {
        System.out.println("Error pulling carousel metrics from MySQL: " + e.getMessage());
    }

    // Default slide message if history table is blank
    if (userQueries.isEmpty()) {
        userQueries.add("Explore something new today!");
    }

    // 2. Setup the CardLayout deck panel
    CardLayout sliderLayout = new CardLayout();
    JPanel cardsDeck = new JPanel(sliderLayout);
    cardsDeck.setOpaque(false);
    cardsDeck.setBounds(0, 0, widgetWidth, widgetHeight);

    // 3. Loop through queries and create self-contained slides
    for (int i = 0; i < userQueries.size(); i++) {
        final String searchString = userQueries.get(i);
        
        // Robust string processing: isolates "dubai" from "dubai, uae", removes symbols, and lowercases it
        String cleanName = searchString.split(",")[0].replaceAll("[^a-zA-Z]", "").trim().toLowerCase();
        
        // 🌟 SAFETY RUNTIME CHECK: 
        // If your files are in the icons folder, change "/backgrounds/" to "/icons/" below!
        String assetPath = "/com/travelsphere/assets/icons/" + cleanName + ".jpg";

        // Create a self-contained card panel that handles its own text absolute placements
        JPanel singleCardSlide = new JPanel(null) {
            private Image cardBg = null;
            {
                try {
                    java.net.URL resource = getClass().getResource(assetPath);
                    
                    // 🔍 NETBEANS CONSOLE DEBUG PRINTER:
                    // This outputs exactly where the app is searching for files. 
                    // If it says "MISSING", verify your folder path structure!
                    if (resource != null) {
                        System.out.println("Carousel loaded image asset successfully: " + assetPath);
                    } else {
                        System.out.println("CAROUSEL IMAGE ASSET MISSING (Using Map Fallback): " + assetPath);
                        resource = getClass().getResource("/com/travelsphere/assets/icons/koyoto.jpg");
                    }
                    
                    if (resource != null) {
                        cardBg = new ImageIcon(resource).getImage();
                    }
                } catch (Exception e) {
                    System.out.println("Slide asset image failed to load.");
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                
                int w = widgetWidth;
                int h = widgetHeight;
                
                // Keep rounded arc style uniform across widgets
                g2.setClip(new java.awt.geom.RoundRectangle2D.Float(0, 0, w, h, 25, 25));
                
                // Base solid background fill color
                g2.setColor(new Color(15, 23, 42));
                g2.fillRect(0, 0, w, h);
                
                if (cardBg != null) {
                    int imgW = cardBg.getWidth(null);
                    int imgH = cardBg.getHeight(null);
                    
                    if (imgW > 0 && imgH > 0) {
                        // Scales background to cleanly cover the entire visible widget container area
                        double scaleX = (double) w / imgW;
                        double scaleY = (double) h / imgH;
                        double scale = Math.max(scaleX, scaleY);
                        
                        int renderW = (int) (imgW * scale);
                        int renderH = (int) (imgH * scale);
                        
                        int x = (w - renderW) / 2;
                        int y = (h - renderH) / 2;
                        
                        g2.drawImage(cardBg, x, y, renderW, renderH, null);
                        
                        // Dark frosted glass overlay to make white headers pop out crisp and clear
                        g2.setColor(new Color(15, 23, 42, 130)); 
                        g2.fillRect(0, 0, w, h);
                    }
                }
                
                // Translucent border stroke line outline
                g2.setColor(new Color(255, 255, 255, 45));
                g2.drawRoundRect(0, 0, w - 1, h - 1, 25, 25);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        singleCardSlide.setOpaque(false);
        singleCardSlide.setBounds(0, 0, widgetWidth, widgetHeight);

        // Header Title Text inside the panel card
        JLabel slideHeader = new JLabel("Recent Activity");
        slideHeader.setFont(new Font("Segoe UI", Font.BOLD, 16));
        slideHeader.setForeground(new Color(241, 245, 249, 220));
        slideHeader.setBounds(25, 22, 220, 25);
        singleCardSlide.add(slideHeader);

        // Core Query Text String Label
        String stylizedTitle = searchString.substring(0, 1).toUpperCase() + searchString.substring(1);
        JLabel queryTextLabel = new JLabel(stylizedTitle);
        queryTextLabel.setFont(new Font("Segoe UI", Font.BOLD, 24)); 
        queryTextLabel.setForeground(Color.WHITE);
        queryTextLabel.setBounds(25, 52, 220, 35);
        
        // Interactive click-to-search action
        queryTextLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (centralizedSearchField != null && !searchString.contains("Explore something")) {
                    centralizedSearchField.setText(searchString);
                    centralizedSearchField.setForeground(Color.WHITE);
                    centralizedSearchField.postActionEvent(); 
                }
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                queryTextLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                queryTextLabel.setForeground(new Color(56, 189, 248)); 
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                queryTextLabel.setForeground(Color.WHITE);
            }
        });
        singleCardSlide.add(queryTextLabel);

        cardsDeck.add(singleCardSlide, "card_" + i);
    }
    
    recentSearchesWidget.add(cardsDeck);

    // 4. Render Carousel Navigation Dot Row Panel
    JPanel dotsRowContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
    dotsRowContainer.setOpaque(false);
    dotsRowContainer.setBounds(0, widgetHeight - 26, widgetWidth, 20);
    
    JLabel[] carouselDots = new JLabel[userQueries.size()];
    for (int i = 0; i < userQueries.size(); i++) {
        final int slideIndex = i;
        carouselDots[i] = new JLabel("●");
        carouselDots[i].setFont(new Font("Segoe UI", Font.BOLD, 11));
        carouselDots[i].setForeground(i == 0 ? new Color(165, 243, 252) : new Color(255, 255, 255, 80));
        carouselDots[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        carouselDots[i].addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                sliderLayout.show(cardsDeck, "card_" + slideIndex);
                for (int k = 0; k < carouselDots.length; k++) {
                    carouselDots[k].setForeground(k == slideIndex ? new Color(165, 243, 252) : new Color(255, 255, 255, 80));
                }
            }
        });
        dotsRowContainer.add(carouselDots[i]);
    }
    
    // Ensure the interactive navigation dot row is added over the card deck layer
    recentSearchesWidget.add(dotsRowContainer);
    recentSearchesWidget.setComponentZOrder(dotsRowContainer, 0);

    // 5. Setup Auto-Rotation Loop Timer Engine
    final int slideCount = userQueries.size();
    if (slideCount > 1) {
        carouselTimer = new Timer(4000, new java.awt.event.ActionListener() {
            private int trackingIndex = 0;
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                trackingIndex = (trackingIndex + 1) % slideCount;
                sliderLayout.show(cardsDeck, "card_" + trackingIndex);
                
                for (int k = 0; k < carouselDots.length; k++) {
                    carouselDots[k].setForeground(k == trackingIndex ? new Color(165, 243, 252) : new Color(255, 255, 255, 80));
                }
            }
        });
        carouselTimer.start();
    }

    // Refresh UI tree structures to invoke immediate rendering
    recentSearchesWidget.revalidate();
    recentSearchesWidget.repaint();
}
    private void injectFilteredFallbackDestinations(JPanel grid, String filterText) {
    grid.removeAll();
    String[][] items = {
        {"Amalfi, Italy", "Beautiful coastline view...", "4.9", "/com/travelsphere/assets/icons/amalfi.jpg"},
        {"Kyoto, Japan", "Bamboo forests...", "4.7", "/com/travelsphere/assets/icons/koyoto.jpg"},
        {"Paris, France", "Eiffel Tower tours...", "4.8", "/com/travelsphere/assets/icons/paris.jpg"},
        {"London, UK", "Big Ben views...", "4.9", "/com/travelsphere/assets/icons/london.jpg"}
    };

    for (String[] data : items) {
        if (filterText != null && !filterText.trim().isEmpty() && !filterText.startsWith("Search")) {
            String match = filterText.toLowerCase().trim();
            if (!data[0].toLowerCase().contains(match) && !data[1].toLowerCase().contains(match)) {
                continue;
            }
        }
        grid.add(createDestinationCard(data[0], data[1], Float.parseFloat(data[2]), data[3]));
    }
    grid.revalidate(); grid.repaint();
}

    private void injectFallbackHotels(JPanel grid, String filterText) {
    grid.removeAll();
    int cardWidth = 240, cardHeight = 190, gapX = 25, gapY = 20, index = 0;
    
    String[][] fallbacks = {
        {"Monastero Santa Rosa", "Amalfi Coast, Italy • $850/night", "4.9", "/com/travelsphere/assets/icons/amalfi.jpg"},
        {"Hoshinoya Ryokan", "Kyoto, Japan • $620/night", "4.8", "/com/travelsphere/assets/icons/koyoto.jpg"}
    };

    for (String[] data : fallbacks) {
        if (filterText != null && !filterText.trim().isEmpty() && !filterText.startsWith("Search")) {
            String match = filterText.toLowerCase().trim();
            if (!data[0].toLowerCase().contains(match) && !data[1].toLowerCase().contains(match)) {
                continue;
            }
        }

        JPanel card = createDestinationCard(data[0], data[1], Float.parseFloat(data[2]), data[3]);
        int x = (index % 2) * (cardWidth + gapX);
        int y = (index / 2) * (cardHeight + gapY);
        card.setBounds(x, y, cardWidth, cardHeight);
        grid.add(card);
        index++;
    }
    int totalRows = (int) Math.ceil((double) index / 2);
    grid.setPreferredSize(new Dimension(530, totalRows * (cardHeight + gapY) + 20));
    grid.revalidate(); grid.repaint();
}
  
  private JMenuItem createPopupMenuItem(String text, String iconPath) {
    JMenuItem item = new JMenuItem(text);
    item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    item.setForeground(Color.WHITE);
    item.setBackground(new Color(32, 41, 54)); // Matches your deep dark glassmorphic panels
    item.setOpaque(true);
    item.setPreferredSize(new Dimension(190, 36));
    
    // Safely load and scale the icon for the dropdown menu item
    try {
        java.net.URL imgUrl = getClass().getResource(iconPath);
        if (imgUrl != null) {
            Image scaledImg = new ImageIcon(imgUrl).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
            item.setIcon(new ImageIcon(scaledImg));
        }
    } catch (Exception ex) {
        // Fallback gracefully if the icon fails to load
    }
    
    return item;
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
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DashboardFram.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        EventQueue.invokeLater(() -> {
            // EXACT NAME FROM YOUR MAIN METHOD WRAPPER USED HERE
            new DashboardFram().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
   public int getLoggedInUserId() {
    return this.loggedInUserId;
} 
}

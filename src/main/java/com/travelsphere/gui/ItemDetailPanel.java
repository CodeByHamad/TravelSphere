package com.travelsphere.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;

/**
 * Clean Dynamic Detail Panel supporting full-window vertical scrolling 
 * and perfect edge-to-edge aspect ratio image grids with stable, jitter-free hover cards.
 * @author Ahsan
 */
public class ItemDetailPanel extends JPanel {
    private final DashboardFram dashboardFrame;
    private final JPanel centerContentDeck;       
    private final CardLayout contentDeckLayout;   
    private final String itemTitle;
    private final int itemId;
    private final boolean isHotel;
    private final String targetBackRouteKey; 

    // UI Elements
    private JLabel titleLabel;
    private JPanel imagesGridContainer;
    private JTextArea descriptionArea;
    private JButton backButton;
    private JButton bookNowButton;
    
    // Track paths to dynamically resize images on-the-fly
    private final ArrayList<String> loadedImagePaths = new ArrayList<>();

    public ItemDetailPanel(DashboardFram dashboardFrame, JPanel centerContentDeck, CardLayout contentDeckLayout, String itemTitle, int itemId, boolean isHotel) {
        this(dashboardFrame, centerContentDeck, contentDeckLayout, itemTitle, itemId, isHotel, isHotel ? "HOTELS" : "EXPLORE");
    }

    public ItemDetailPanel(DashboardFram dashboardFrame, JPanel centerContentDeck, CardLayout contentDeckLayout, String itemTitle, int itemId, boolean isHotel, String targetBackRouteKey) {
        this.dashboardFrame = dashboardFrame; 
        this.centerContentDeck = centerContentDeck;
        this.contentDeckLayout = contentDeckLayout;
        this.itemTitle = itemTitle;
        this.itemId = itemId;
        this.isHotel = isHotel;
        this.targetBackRouteKey = (targetBackRouteKey != null) ? targetBackRouteKey : (isHotel ? "HOTELS" : "EXPLORE");

        setOpaque(false); 
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        initUI();
        loadSpecificDescription();
        loadSpecificImages();
    }

    private void initUI() {
        // --- TOP TITLE ROW ---
        titleLabel = new JLabel(itemTitle);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

        // --- MAIN BODY SCROLLABLE PANELS ---
        JPanel mainScrollableBody = new JPanel();
        mainScrollableBody.setOpaque(false);
        mainScrollableBody.setLayout(new BoxLayout(mainScrollableBody, BoxLayout.Y_AXIS));

        // 1. Description Field Overview Card
        descriptionArea = new JTextArea();
        descriptionArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        descriptionArea.setForeground(new Color(230, 235, 240));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(true);
        descriptionArea.setBackground(new Color(30, 41, 59, 120)); // Subtle dark glass backdrop
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel descWrapper = new JPanel(new BorderLayout());
        descWrapper.setOpaque(false);
        descWrapper.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 30), 1, true),
                "Overview Details", 0, 0, new Font("Segoe UI", Font.BOLD, 13), Color.WHITE));
        descWrapper.add(descriptionArea, BorderLayout.CENTER);
        
        descWrapper.setMinimumSize(new Dimension(200, 140));
        descWrapper.setPreferredSize(new Dimension(400, 160));
        descWrapper.setMaximumSize(new Dimension(Short.MAX_VALUE, 200));

        mainScrollableBody.add(descWrapper);
        mainScrollableBody.add(Box.createVerticalStrut(20)); // Margin divider

        // 2. Image Grid Container Setup
        imagesGridContainer = new JPanel(new GridLayout(0, 3, 15, 15));
        imagesGridContainer.setOpaque(false);
        mainScrollableBody.add(imagesGridContainer);

        JScrollPane masterScrollPane = new JScrollPane(mainScrollableBody);
        masterScrollPane.setOpaque(false);
        masterScrollPane.getViewport().setOpaque(false);
        masterScrollPane.setBorder(null);
        masterScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        masterScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        masterScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(masterScrollPane, BorderLayout.CENTER);

        // --- BOTTOM NAVIGATION ACTION BAR ---
        JPanel buttonControllerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 0));
        buttonControllerPanel.setOpaque(false);
        Dimension uniformButtonSize = new Dimension(130, 38);

        // Back Button
        backButton = new JButton("← Back");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBackground(new Color(51, 65, 85)); 
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 20), 1, true));
        backButton.setPreferredSize(uniformButtonSize); 
        backButton.addActionListener(e -> {
            contentDeckLayout.show(centerContentDeck, this.targetBackRouteKey);
            centerContentDeck.remove(this);
            centerContentDeck.revalidate();
            centerContentDeck.repaint();
            if (dashboardFrame != null) {
                dashboardFrame.revalidate();
                dashboardFrame.repaint();
            }
        });

        // Book Now Button
        bookNowButton = new JButton("Book Now →");
        bookNowButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bookNowButton.setBackground(new Color(0, 102, 204)); 
        bookNowButton.setForeground(Color.WHITE);
        bookNowButton.setFocusPainted(false);
        bookNowButton.setBorder(BorderFactory.createEmptyBorder());
        bookNowButton.setPreferredSize(uniformButtonSize); 
        bookNowButton.addActionListener(e -> {
            String cleanSearchKey = itemTitle;
            if (!isHotel && itemTitle.contains(",")) {
                cleanSearchKey = itemTitle.split(",")[0].trim();
            }

            for (Component comp : centerContentDeck.getComponents()) {
                if ("bookingViewCard".equals(comp.getName()) || comp instanceof BookingPanel) {
                    centerContentDeck.remove(comp);
                }
            }

            BookingPanel bookingView = new BookingPanel(
                dashboardFrame, centerContentDeck, contentDeckLayout, cleanSearchKey, isHotel, this.targetBackRouteKey
            );
            bookingView.setName("bookingViewCard");
            centerContentDeck.add(bookingView, "bookingView");
            contentDeckLayout.show(centerContentDeck, "bookingView");
            centerContentDeck.revalidate();
            centerContentDeck.repaint();
        });

        buttonControllerPanel.add(backButton);
        buttonControllerPanel.add(bookNowButton);
        add(buttonControllerPanel, BorderLayout.SOUTH);

        // Dynamic Resize Listener
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                renderResponsiveGridImages();
            }
        });
    }

    public void loadSpecificImages() {
        String tableName = isHotel ? "hotel_images" : "destination_images";
        String idColumn = isHotel ? "hotel_id" : "destination_id";
        String sql = "SELECT image_path FROM " + tableName + " WHERE " + idColumn + " = ?";

        loadedImagePaths.clear();
        try (Connection conn = com.travelsphere.database.DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, itemId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                loadedImagePaths.add(rs.getString("image_path"));
            }
            renderResponsiveGridImages();
        } catch (SQLException e) {
            System.out.println("SQL Error tracking images: " + e.getMessage());
        }
    }

    /**
     * Renders grid cells inside individual sub-panel cards using a stable layout design
     * that eliminates flickering or shifting artifacts during mouse hover events.
     */
    private void renderResponsiveGridImages() {
        if (imagesGridContainer == null) return;
        imagesGridContainer.removeAll();

        if (loadedImagePaths.isEmpty()) {
            JLabel fallbackLabel = new JLabel("📸 More gallery pictures coming soon for this location.");
            fallbackLabel.setForeground(Color.LIGHT_GRAY);
            fallbackLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            imagesGridContainer.add(fallbackLabel);
        } else {
            int componentWidth = imagesGridContainer.getWidth();
            if (componentWidth <= 0) componentWidth = 800; 
            
            int cellWidth = (componentWidth - 30) / 3; 
            int cellHeight = (int) (cellWidth * 0.62);   

            // 🛠️ BALANCED STRUCTURAL BORDERS
            // Normal state: 1px outer line + 1px interior empty padding = 2px footprint total
            final Border normalBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 45), 1, true),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)
            );
            // Hover state: 2px solid vivid neon blue border highlight = 2px footprint total
            final Border hoverBorder = BorderFactory.createLineBorder(new Color(102, 192, 255, 200), 2, true);
            
            final Color normalBg = new Color(255, 255, 255, 0); 
            final Color hoverBg = new Color(255, 255, 255, 15);  // Frosted-glass brightening mask

            for (String path : loadedImagePaths) {
                java.net.URL url = getClass().getResource(path);
                if (url != null) {
                    // Create an individual standalone panel card wrapper
                    JPanel imageCard = new JPanel(new BorderLayout());
                    imageCard.setOpaque(true);
                    imageCard.setBackground(normalBg);
                    imageCard.setBorder(normalBorder);

                    JLabel imageLabel = new JLabel();
                    ImageIcon icon = new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(cellWidth, cellHeight, Image.SCALE_SMOOTH));
                    imageLabel.setIcon(icon);
                    imageCard.add(imageLabel, BorderLayout.CENTER);

                    // Attach Hover Listener
                    MouseAdapter cardHoverAdapter = new MouseAdapter() {
                        @Override
                        public void mouseEntered(MouseEvent evt) {
                            imageCard.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                            imageCard.setBorder(hoverBorder);
                            imageCard.setBackground(hoverBg);
                            imageCard.repaint();
                        }

                        @Override
                        public void mouseExited(MouseEvent evt) {
                            imageCard.setBorder(normalBorder);
                            imageCard.setBackground(normalBg);
                            imageCard.repaint();
                        }
                    };

                    imageCard.addMouseListener(cardHoverAdapter);
                    imageLabel.addMouseListener(cardHoverAdapter); // Ensure smooth event handling on raw image intersection

                    imagesGridContainer.add(imageCard);
                }
            }
        }
        imagesGridContainer.revalidate();
        imagesGridContainer.repaint();
    }

    public void loadSpecificDescription() {
        String sql = isHotel ? "SELECT amenities FROM hotels WHERE hotel_id = ?" : "SELECT description FROM destinations WHERE destination_id = ?";
        
        try (Connection conn = com.travelsphere.database.DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, itemId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String rawDesc = isHotel ? rs.getString("amenities") : rs.getString("description");
                descriptionArea.setText(rawDesc);
            }
        } catch (SQLException e) {
            descriptionArea.setText("Failed to load details overview.");
        }
    }
}
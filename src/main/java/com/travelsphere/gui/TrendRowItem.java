package com.travelsphere.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TrendRowItem extends JPanel {
    private final String destination;
    private final int growthPercentage;
    private final String metricText;
    private final Runnable refreshCallback; // 🌟 4th Property: Holds the click callback function

    // 🛠️ FIXED CONSTRUCTOR: Now accepts 4 parameters to match DashboardFram
    public TrendRowItem(String destination, int growthPercentage, String metricText, Runnable refreshCallback) {
        this.destination = destination;
        this.growthPercentage = Math.min(100, Math.max(0, growthPercentage));
        this.metricText = metricText;
        this.refreshCallback = refreshCallback;

        setOpaque(false);
        setLayout(new BorderLayout(10, 6));
        setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        initComponentUI();
        setupHoverInteractions();
    }

    private void initComponentUI() {
        JPanel textLayer = new JPanel(new BorderLayout());
        textLayer.setOpaque(false);

        JLabel nameLabel = new JLabel(destination);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(Color.WHITE);

        JLabel statsLabel = new JLabel(metricText);
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statsLabel.setForeground(new Color(148, 163, 184)); 

        textLayer.add(nameLabel, BorderLayout.WEST);
        textLayer.add(statsLabel, BorderLayout.EAST);
        add(textLayer, BorderLayout.NORTH);

        // Custom Progress Pill Bar Paint Channel
        JComponent customProgressBar = new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                int arc = h; 

                // Track Background Line
                g2d.setColor(new Color(255, 255, 255, 20));
                g2d.fillRoundRect(0, 0, w, h, arc, arc);

                // Dynamic Gradient Fill
                int fillWidth = (int) (w * (growthPercentage / 100.0));
                if (fillWidth > 0) {
                    GradientPaint progressGradient = new GradientPaint(
                        0, 0, new Color(56, 189, 248), 
                        fillWidth, 0, new Color(0, 102, 204) 
                    );
                    g2d.setPaint(progressGradient);
                    g2d.fillRoundRect(0, 0, fillWidth, h, arc, arc);
                }
                g2d.dispose();
            }
        };
        customProgressBar.setPreferredSize(new Dimension(100, 6)); 
        add(customProgressBar, BorderLayout.CENTER);
    }

    private void setupHoverInteractions() {
        final Border normalBorder = BorderFactory.createEmptyBorder(8, 12, 8, 12);
        final Border hoverBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(102, 192, 255, 60), 1, true),
            BorderFactory.createEmptyBorder(7, 11, 7, 11) 
        );

        MouseAdapter hoverAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 🌟 EXECUTED ON CLICK: Triggers the refresh loop we fed down from DashboardFram
                if (refreshCallback != null) {
                    refreshCallback.run();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                setBorder(hoverBorder);
                setOpaque(true);
                setBackground(new Color(255, 255, 255, 12)); 
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(normalBorder);
                setOpaque(false);
                repaint();
            }
        };
        addMouseListener(hoverAdapter);
    }
}
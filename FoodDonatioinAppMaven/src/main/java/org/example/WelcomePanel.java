package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class WelcomePanel extends JPanel {

    private JTabbedPane mainTabbedPane;

    public WelcomePanel(JTabbedPane tabbedPane) {
        this.mainTabbedPane = tabbedPane;

        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Dashboard", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        cardsPanel.setOpaque(false);

        // Create cards with total counts and the name of the most recent entry
        cardsPanel.add(createDashboardCard("Total Donors", getCount("Donor"),
                "Latest: " + getMostRecentEntry("Donor", "Name", "Registration_Date"),
                "user", 1));

        cardsPanel.add(createDashboardCard("Total Food Items", getCount("Food_Item"),
                "Latest: " + getMostRecentEntry("Food_Item", "Food_Name", "Food_ID"),
                "food", 2));

        cardsPanel.add(createDashboardCard("Total Receivers", getCount("Receiver"),
                "Latest: " + getMostRecentEntry("Receiver", "Name", "Registration_Date"),
                "receiver", 3));

        cardsPanel.add(createDashboardCard("Total Volunteers", getCount("Volunteer"),
                "Latest: " + getMostRecentEntry("Volunteer", "Name", "Volunteer_ID"),
                "volunteer", 4));

        add(cardsPanel, BorderLayout.CENTER);
    }

    private int getCount(String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String getMostRecentEntry(String tableName, String nameColumn, String orderColumn) {
        String sql = "SELECT " + nameColumn + " FROM " + tableName + " ORDER BY " + orderColumn + " DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "N/A"; // Return Not Applicable if there's an error or no entries
    }

    private JPanel createDashboardCard(String title, int count, String recentInfo, String iconName, int tabIndex) {
        GuiUtils.RoundedPanel card = new GuiUtils.RoundedPanel(15);
        card.setBackground(GuiUtils.PRIMARY_COLOR);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(GuiUtils.PRIMARY_COLOR, 2),
                new EmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- Top section with Icon and Title ---
        JPanel topPanel = new JPanel(new BorderLayout(15, 0));
        topPanel.setOpaque(false);
        JLabel iconLabel = new JLabel(GuiUtils.getIcon(iconName, 32));
        topPanel.add(iconLabel, BorderLayout.WEST);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        card.add(topPanel, BorderLayout.NORTH);

        // --- Center section with Count ---
        JLabel countLabel = new JLabel(String.valueOf(count));
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 60));
        countLabel.setForeground(Color.WHITE);
        countLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(countLabel, BorderLayout.CENTER);

        // --- Bottom section with Recent Info ---
        JLabel recentLabel = new JLabel(recentInfo);
        recentLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        recentLabel.setForeground(GuiUtils.TEXT_COLOR);
        recentLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(recentLabel, BorderLayout.SOUTH);

        // --- Hover and Click Listeners ---
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                mainTabbedPane.setSelectedIndex(tabIndex);
            }
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(GuiUtils.ACCENT_COLOR, 2),
                        new EmptyBorder(20, 20, 20, 20)
                ));
            }
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(GuiUtils.PRIMARY_COLOR, 2),
                        new EmptyBorder(20, 20, 20, 20)
                ));
            }
        });
        return card;
    }
}
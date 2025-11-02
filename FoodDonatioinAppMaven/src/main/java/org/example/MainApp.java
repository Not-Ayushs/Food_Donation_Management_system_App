package org.example;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;

public class MainApp {
    public static void main(String[] args) {
        FlatDarkLaf.setup();
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }

    public static void createAndShowMainGUI() {
        JFrame frame = new JFrame("Food Donation Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1366, 768);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(GuiUtils.SECONDARY_COLOR);
        mainPanel.add(new HeaderPanel(), BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabbedPane.setForeground(GuiUtils.TEXT_COLOR);
        UIManager.put("TabbedPane.selectedForeground", Color.WHITE);
        UIManager.put("TabbedPane.underlineColor", GuiUtils.ACCENT_COLOR);

        // UPDATED: Pass the tabbedPane to the WelcomePanel
        tabbedPane.addTab("Dashboard", GuiUtils.getIcon("home", 20), new WelcomePanel(tabbedPane));
        tabbedPane.addTab("Donors", GuiUtils.getIcon("user", 20), new DonorPanel());
        tabbedPane.addTab("Food Items", GuiUtils.getIcon("food", 20), new FoodItemPanel());
        tabbedPane.addTab("Receivers", GuiUtils.getIcon("receiver", 20), new ReceiverPanel());
        tabbedPane.addTab("Volunteers", GuiUtils.getIcon("volunteer", 20), new VolunteerPanel());
        tabbedPane.addTab("Reports", GuiUtils.getIcon("report", 20), new ReportsPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
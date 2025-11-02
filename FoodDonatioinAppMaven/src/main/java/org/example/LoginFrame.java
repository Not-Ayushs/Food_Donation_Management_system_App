package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField userField;
    private JPasswordField passField;

    public LoginFrame() {
        setTitle("Login - Food Donation System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 280);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Application Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(GuiUtils.ACCENT_COLOR);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 10, 15));
        fieldsPanel.setOpaque(false);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(GuiUtils.LABEL_FONT);
        userField = new JTextField(); // Changed from GuiUtils for simplicity here
        userField.setFont(GuiUtils.INPUT_FONT);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(GuiUtils.LABEL_FONT);
        passField = new JPasswordField();
        passField.setFont(GuiUtils.INPUT_FONT);

        fieldsPanel.add(userLabel);
        fieldsPanel.add(userField);
        fieldsPanel.add(passLabel);
        fieldsPanel.add(passField);
        mainPanel.add(fieldsPanel, BorderLayout.CENTER);

        JButton loginButton = GuiUtils.createStyledButton("Login", GuiUtils.ACCENT_COLOR, "login");
        mainPanel.add(loginButton, BorderLayout.SOUTH);

        add(mainPanel);

        loginButton.addActionListener(e -> performLogin());
    }

    private void performLogin() {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        if (authenticate(username, password)) {
            dispose();
            MainApp.createAndShowMainGUI();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        // CORRECTED: The database call is now wrapped in a try-with-resources block
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // True if a user was found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Show a more specific error if the database connection fails
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
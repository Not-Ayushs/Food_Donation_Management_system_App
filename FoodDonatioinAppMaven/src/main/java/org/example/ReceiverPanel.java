package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class ReceiverPanel extends JPanel {
    private JTable table;
    private JTextField idField, nameField, contactPersonField, emailField, phoneField, typeField, locationField, ratingField;
    private JComboBox<String> verifiedComboBox;
    private GuiUtils.PlaceholderTextField searchField;

    public ReceiverPanel() {
        setLayout(new BorderLayout(20, 20));
        setOpaque(false);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.65);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);

        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setOpaque(false);

        table = new JTable() { public boolean isCellEditable(int row, int column) { return false; }};
        GuiUtils.styleTable(table);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setOpaque(false);
        searchField = new GuiUtils.PlaceholderTextField("Search by name or location...");
        JButton searchButton = GuiUtils.createStyledButton("", GuiUtils.ACCENT_COLOR, "search");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        splitPane.setLeftComponent(tablePanel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(GuiUtils.BORDER_COLOR), "Receiver Details", 0, 0, GuiUtils.LABEL_FONT, GuiUtils.TEXT_COLOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        idField = new JTextField(); idField.setEditable(false);
        nameField = new JTextField();
        contactPersonField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        typeField = new JTextField();
        locationField = new JTextField();
        ratingField = new JTextField();
        verifiedComboBox = GuiUtils.createStyledComboBox(new String[]{"Yes", "No"});

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; formPanel.add(idField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(contactPersonField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(emailField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; formPanel.add(phoneField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; formPanel.add(typeField, gbc);
        gbc.gridx = 0; gbc.gridy = 6; formPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; formPanel.add(locationField, gbc);
        gbc.gridx = 0; gbc.gridy = 7; formPanel.add(new JLabel("Verified:"), gbc);
        gbc.gridx = 1; gbc.gridy = 7; formPanel.add(verifiedComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 8; formPanel.add(new JLabel("Rating:"), gbc);
        gbc.gridx = 1; gbc.gridy = 8; formPanel.add(ratingField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        JButton addButton = GuiUtils.createStyledButton("Add", new Color(40, 167, 69), "add");
        JButton updateButton = GuiUtils.createStyledButton("Save", GuiUtils.ACCENT_COLOR, "save");
        JButton deleteButton = GuiUtils.createStyledButton("Delete", new Color(220, 53, 69), "delete");
        JButton clearButton = GuiUtils.createStyledButton("Clear", new Color(108, 117, 125), "clear");
        buttonPanel.add(clearButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(addButton);

        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.SOUTHEAST; gbc.weighty = 1.0;
        formPanel.add(buttonPanel, gbc);

        splitPane.setRightComponent(formPanel);
        add(splitPane, BorderLayout.CENTER);

        loadData();

        addButton.addActionListener(e -> addReceiver());
        updateButton.addActionListener(e -> updateReceiver());
        deleteButton.addActionListener(e -> deleteReceiver());
        clearButton.addActionListener(e -> clearForm());
        searchButton.addActionListener(e -> searchReceivers());
        searchField.addActionListener(e -> searchReceivers());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRowInView = table.getSelectedRow();
                if (selectedRowInView != -1) {
                    int modelRow = table.convertRowIndexToModel(selectedRowInView);
                    DefaultTableModel currentModel = (DefaultTableModel) table.getModel();
                    idField.setText(currentModel.getValueAt(modelRow, 0).toString());
                    nameField.setText(currentModel.getValueAt(modelRow, 1).toString());
                    contactPersonField.setText(currentModel.getValueAt(modelRow, 2) != null ? currentModel.getValueAt(modelRow, 2).toString() : "");
                    emailField.setText(currentModel.getValueAt(modelRow, 3) != null ? currentModel.getValueAt(modelRow, 3).toString() : "");
                    phoneField.setText(currentModel.getValueAt(modelRow, 4) != null ? currentModel.getValueAt(modelRow, 4).toString() : "");
                    typeField.setText(currentModel.getValueAt(modelRow, 5) != null ? currentModel.getValueAt(modelRow, 5).toString() : "");
                    locationField.setText(currentModel.getValueAt(modelRow, 6) != null ? currentModel.getValueAt(modelRow, 6).toString() : "");
                    verifiedComboBox.setSelectedItem(currentModel.getValueAt(modelRow, 7).toString());
                    ratingField.setText(currentModel.getValueAt(modelRow, 9) != null ? currentModel.getValueAt(modelRow, 9).toString() : "");
                }
            }
        });
    }

    private void searchReceivers() {
        String searchTerm = searchField.getText();
        if (searchTerm.equals("Search by name or location...")) searchTerm = "";
        String sql = "SELECT * FROM Receiver WHERE Name LIKE ? OR Location LIKE ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchTerm + "%");
            pstmt.setString(2, "%" + searchTerm + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                table.setModel(GuiUtils.buildTableModel(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during search: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadData() {
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Receiver")) {
            table.setModel(GuiUtils.buildTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading receivers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getNextId() {
        int maxId = 0;
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT MAX(Receiver_ID) FROM Receiver")) {
            if (rs.next()) maxId = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxId + 1;
    }

    private void addReceiver() {
        String sql = "INSERT INTO Receiver (Receiver_ID, Name, Contact_Person, Email, Phone_No, Type, Location, Verified_Status, Registration_Date, Rating) VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURDATE(), ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, getNextId());
            pstmt.setString(2, nameField.getText());
            pstmt.setString(3, contactPersonField.getText());
            pstmt.setString(4, emailField.getText());
            pstmt.setString(5, phoneField.getText());
            pstmt.setString(6, typeField.getText());
            pstmt.setString(7, locationField.getText());
            pstmt.setString(8, (String) verifiedComboBox.getSelectedItem());
            pstmt.setBigDecimal(9, new java.math.BigDecimal(ratingField.getText()));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Receiver added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error adding receiver: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateReceiver() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a receiver to update.", "No Receiver Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String sql = "UPDATE Receiver SET Name=?, Contact_Person=?, Email=?, Phone_No=?, Type=?, Location=?, Verified_Status=?, Rating=? WHERE Receiver_ID=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, contactPersonField.getText());
            pstmt.setString(3, emailField.getText());
            pstmt.setString(4, phoneField.getText());
            pstmt.setString(5, typeField.getText());
            pstmt.setString(6, locationField.getText());
            pstmt.setString(7, (String) verifiedComboBox.getSelectedItem());
            pstmt.setBigDecimal(8, new java.math.BigDecimal(ratingField.getText()));
            pstmt.setInt(9, Integer.parseInt(idField.getText()));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Receiver updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error updating receiver: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteReceiver() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a receiver to delete.", "No Receiver Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM Receiver WHERE Receiver_ID = ?";
            try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(idField.getText()));
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Receiver deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                clearForm();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting receiver: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        contactPersonField.setText("");
        emailField.setText("");
        phoneField.setText("");
        typeField.setText("");
        locationField.setText("");
        verifiedComboBox.setSelectedIndex(0);
        ratingField.setText("");
        searchField.setText("Search by name or location...");
        searchField.setForeground(Color.GRAY);
        table.clearSelection();
    }
}
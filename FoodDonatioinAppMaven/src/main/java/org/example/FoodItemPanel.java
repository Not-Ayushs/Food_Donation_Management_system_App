package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class FoodItemPanel extends JPanel {
    private JTable table;
    private JTextField foodIdField, nameField, quantityField, expiryDateField, cookedTimeField, donorIdField;
    private JComboBox<String> categoryComboBox, statusComboBox;
    private GuiUtils.PlaceholderTextField searchField;

    public FoodItemPanel() {
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
        searchField = new GuiUtils.PlaceholderTextField("Search by name or category...");
        JButton searchButton = GuiUtils.createStyledButton("", GuiUtils.ACCENT_COLOR, "search");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        splitPane.setLeftComponent(tablePanel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(GuiUtils.BORDER_COLOR), "Food Item Details", 0, 0, GuiUtils.LABEL_FONT, GuiUtils.TEXT_COLOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        foodIdField = new JTextField(); foodIdField.setEditable(false);
        nameField = new JTextField();
        quantityField = new JTextField();
        expiryDateField = new JTextField("YYYY-MM-DD");
        cookedTimeField = new JTextField("HH:MM:SS");
        donorIdField = new JTextField();
        String[] categories = {"Grains", "Vegetables", "Fruits", "Dairy", "Seafood", "Bakery", "Fast Food", "Beverage"};
        categoryComboBox = GuiUtils.createStyledComboBox(categories);
        statusComboBox = GuiUtils.createStyledComboBox(new String[]{"Available", "Unavailable"});

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; formPanel.add(foodIdField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(quantityField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Expiry:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(expiryDateField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Cooked Time:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; formPanel.add(cookedTimeField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; formPanel.add(categoryComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 6; formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; formPanel.add(statusComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 7; formPanel.add(new JLabel("Donor ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 7; formPanel.add(donorIdField, gbc);

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

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.SOUTHEAST; gbc.weighty = 1.0;
        formPanel.add(buttonPanel, gbc);

        splitPane.setRightComponent(formPanel);
        add(splitPane, BorderLayout.CENTER);

        loadData();

        addButton.addActionListener(e -> addFoodItem());
        updateButton.addActionListener(e -> updateFoodItem());
        deleteButton.addActionListener(e -> deleteFoodItem());
        clearButton.addActionListener(e -> clearForm());
        searchButton.addActionListener(e -> searchFoodItems());
        searchField.addActionListener(e -> searchFoodItems());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRowInView = table.getSelectedRow();
                if (selectedRowInView != -1) {
                    int modelRow = table.convertRowIndexToModel(selectedRowInView);
                    DefaultTableModel currentModel = (DefaultTableModel) table.getModel();
                    foodIdField.setText(currentModel.getValueAt(modelRow, 0).toString());
                    nameField.setText(currentModel.getValueAt(modelRow, 1).toString());
                    quantityField.setText(currentModel.getValueAt(modelRow, 2).toString());
                    expiryDateField.setText(currentModel.getValueAt(modelRow, 3) != null ? currentModel.getValueAt(modelRow, 3).toString() : "YYYY-MM-DD");
                    cookedTimeField.setText(currentModel.getValueAt(modelRow, 4) != null ? currentModel.getValueAt(modelRow, 4).toString() : "HH:MM:SS");
                    categoryComboBox.setSelectedItem(currentModel.getValueAt(modelRow, 5).toString());
                    statusComboBox.setSelectedItem(currentModel.getValueAt(modelRow, 7).toString());
                    donorIdField.setText(currentModel.getValueAt(modelRow, 8) != null ? currentModel.getValueAt(modelRow, 8).toString() : "");
                }
            }
        });
    }

    private void searchFoodItems() {
        String searchTerm = searchField.getText();
        if (searchTerm.equals("Search by name or category...")) searchTerm = "";
        String sql = "SELECT * FROM Food_Item WHERE Food_Name LIKE ? OR Category LIKE ?";
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
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Food_Item")) {
            table.setModel(GuiUtils.buildTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading food items: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addFoodItem() {
        String sql = "INSERT INTO Food_Item (Food_ID, Food_Name, Quantity, Expiry_Date, Cooked_Time, Category, Status, Donor_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, getNextId());
            pstmt.setString(2, nameField.getText());
            pstmt.setInt(3, Integer.parseInt(quantityField.getText()));
            pstmt.setDate(4, Date.valueOf(expiryDateField.getText()));
            pstmt.setTime(5, Time.valueOf(cookedTimeField.getText()));
            pstmt.setString(6, (String) categoryComboBox.getSelectedItem());
            pstmt.setString(7, (String) statusComboBox.getSelectedItem());
            pstmt.setInt(8, Integer.parseInt(donorIdField.getText()));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Food Item added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        } catch (SQLException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error adding food item: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getNextId() {
        int maxId = 0;
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT MAX(Food_ID) FROM Food_Item")) {
            if (rs.next()) maxId = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxId + 1;
    }

    private void updateFoodItem() {
        if (foodIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an item to update.", "No Item Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String sql = "UPDATE Food_Item SET Food_Name=?, Quantity=?, Expiry_Date=?, Cooked_Time=?, Category=?, Status=?, Donor_ID=? WHERE Food_ID=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nameField.getText());
            pstmt.setInt(2, Integer.parseInt(quantityField.getText()));
            pstmt.setDate(3, Date.valueOf(expiryDateField.getText()));
            pstmt.setTime(4, Time.valueOf(cookedTimeField.getText()));
            pstmt.setString(5, (String) categoryComboBox.getSelectedItem());
            pstmt.setString(6, (String) statusComboBox.getSelectedItem());
            pstmt.setInt(7, Integer.parseInt(donorIdField.getText()));
            pstmt.setInt(8, Integer.parseInt(foodIdField.getText()));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Food Item updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        } catch (SQLException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error updating food item: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteFoodItem() {
        if (foodIdField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an item to delete.", "No Item Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM Food_Item WHERE Food_ID = ?";
            try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(foodIdField.getText()));
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Food Item deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                clearForm();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting food item: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        foodIdField.setText("");
        nameField.setText("");
        quantityField.setText("");
        expiryDateField.setText("YYYY-MM-DD");
        cookedTimeField.setText("HH:MM:SS");
        donorIdField.setText("");
        categoryComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);
        searchField.setText("Search by name or category...");
        searchField.setForeground(Color.GRAY);
        table.clearSelection();
    }
}
package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class VolunteerPanel extends JPanel {
    private JTable table;
    private JTextField idField, nameField, phoneField, areaAssignedField, vehicleTypeField, ratingField;
    private JComboBox<String> availabilityComboBox;
    private GuiUtils.PlaceholderTextField searchField;

    public VolunteerPanel() {
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
        searchField = new GuiUtils.PlaceholderTextField("Search by name or area...");
        JButton searchButton = GuiUtils.createStyledButton("", GuiUtils.ACCENT_COLOR, "search");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        splitPane.setLeftComponent(tablePanel);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(GuiUtils.BORDER_COLOR), "Volunteer Details", 0, 0, GuiUtils.LABEL_FONT, GuiUtils.TEXT_COLOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        idField = new JTextField(); idField.setEditable(false);
        nameField = new JTextField();
        phoneField = new JTextField();
        areaAssignedField = new JTextField();
        vehicleTypeField = new JTextField();
        ratingField = new JTextField();
        availabilityComboBox = GuiUtils.createStyledComboBox(new String[]{"Available", "Unavailable"});

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST; formPanel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; formPanel.add(idField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(nameField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(phoneField, gbc);
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(new JLabel("Area:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(areaAssignedField, gbc);
        gbc.gridx = 0; gbc.gridy = 4; formPanel.add(new JLabel("Vehicle:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; formPanel.add(vehicleTypeField, gbc);
        gbc.gridx = 0; gbc.gridy = 5; formPanel.add(new JLabel("Availability:"), gbc);
        gbc.gridx = 1; gbc.gridy = 5; formPanel.add(availabilityComboBox, gbc);
        gbc.gridx = 0; gbc.gridy = 6; formPanel.add(new JLabel("Rating:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; formPanel.add(ratingField, gbc);

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

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.SOUTHEAST; gbc.weighty = 1.0;
        formPanel.add(buttonPanel, gbc);

        splitPane.setRightComponent(formPanel);
        add(splitPane, BorderLayout.CENTER);

        loadData();

        addButton.addActionListener(e -> addVolunteer());
        updateButton.addActionListener(e -> updateVolunteer());
        deleteButton.addActionListener(e -> deleteVolunteer());
        clearButton.addActionListener(e -> clearForm());
        searchButton.addActionListener(e -> searchVolunteers());
        searchField.addActionListener(e -> searchVolunteers());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRowInView = table.getSelectedRow();
                if (selectedRowInView != -1) {
                    int modelRow = table.convertRowIndexToModel(selectedRowInView);
                    DefaultTableModel currentModel = (DefaultTableModel) table.getModel();
                    idField.setText(currentModel.getValueAt(modelRow, 0).toString());
                    nameField.setText(currentModel.getValueAt(modelRow, 1).toString());
                    phoneField.setText(currentModel.getValueAt(modelRow, 2) != null ? currentModel.getValueAt(modelRow, 2).toString() : "");
                    areaAssignedField.setText(currentModel.getValueAt(modelRow, 3) != null ? currentModel.getValueAt(modelRow, 3).toString() : "");
                    availabilityComboBox.setSelectedItem(currentModel.getValueAt(modelRow, 4).toString());
                    vehicleTypeField.setText(currentModel.getValueAt(modelRow, 5) != null ? currentModel.getValueAt(modelRow, 5).toString() : "");
                    ratingField.setText(currentModel.getValueAt(modelRow, 6) != null ? currentModel.getValueAt(modelRow, 6).toString() : "");
                }
            }
        });
    }

    private void searchVolunteers() {
        String searchTerm = searchField.getText();
        if (searchTerm.equals("Search by name or area...")) searchTerm = "";
        String sql = "SELECT * FROM Volunteer WHERE Name LIKE ? OR Area_Assigned LIKE ?";
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
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM Volunteer")) {
            table.setModel(GuiUtils.buildTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading volunteers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getNextId() {
        int maxId = 0;
        try (Connection conn = DBConnection.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT MAX(Volunteer_ID) FROM Volunteer")) {
            if (rs.next()) maxId = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maxId + 1;
    }

    private void addVolunteer() {
        String sql = "INSERT INTO Volunteer (Volunteer_ID, Name, Phone_No, Area_Assigned, Availability_Status, Vehicle_Type, Rating) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, getNextId());
            pstmt.setString(2, nameField.getText());
            pstmt.setString(3, phoneField.getText());
            pstmt.setString(4, areaAssignedField.getText());
            pstmt.setString(5, (String) availabilityComboBox.getSelectedItem());
            pstmt.setString(6, vehicleTypeField.getText());
            pstmt.setBigDecimal(7, new java.math.BigDecimal(ratingField.getText()));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Volunteer added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error adding volunteer: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateVolunteer() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a volunteer to update.", "No Volunteer Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String sql = "UPDATE Volunteer SET Name=?, Phone_No=?, Area_Assigned=?, Availability_Status=?, Vehicle_Type=?, Rating=? WHERE Volunteer_ID=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, phoneField.getText());
            pstmt.setString(3, areaAssignedField.getText());
            pstmt.setString(4, (String) availabilityComboBox.getSelectedItem());
            pstmt.setString(5, vehicleTypeField.getText());
            pstmt.setBigDecimal(6, new java.math.BigDecimal(ratingField.getText()));
            pstmt.setInt(7, Integer.parseInt(idField.getText()));
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Volunteer updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error updating volunteer: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteVolunteer() {
        if (idField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a volunteer to delete.", "No Volunteer Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM Volunteer WHERE Volunteer_ID = ?";
            try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, Integer.parseInt(idField.getText()));
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Volunteer deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                clearForm();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting volunteer: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        phoneField.setText("");
        areaAssignedField.setText("");
        availabilityComboBox.setSelectedIndex(0);
        vehicleTypeField.setText("");
        ratingField.setText("");
        searchField.setText("Search by name or area...");
        searchField.setForeground(Color.GRAY);
        table.clearSelection();
    }
}
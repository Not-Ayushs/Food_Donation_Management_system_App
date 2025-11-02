package org.example;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReportsPanel extends JPanel {
    private JComboBox<String> reportComboBox;
    private JTextArea queryTextArea;
    private JTable resultTable;

    private final String[] reportNames = {
            "1. Available Food from Organizations",
            "2. Pickup Requests per Receiver",
            "3. Volunteers with Completed Deliveries",
            "4. High-Rated Donors & Donation Count",
            "5. Food Items Expiring in 7 Days"
    };

    private final String[] queries = {
            "SELECT f.Food_Name, f.Quantity, f.Expiry_Date, d.Name AS Donor_Name FROM Food_Item f JOIN Donor d ON f.Donor_ID = d.Donor_ID WHERE d.Type = 'Organization' AND f.Status = 'Available';",
            "SELECT r.Name, COUNT(p.Pickup_ID) AS NumberOfRequests FROM Receiver r JOIN Pickup_Request p ON r.Receiver_ID = p.Receiver_ID GROUP BY r.Name ORDER BY NumberOfRequests DESC;",
            "SELECT v.Name AS Volunteer_Name, v.Phone_No, r.Name AS Receiver_Name, r.Location FROM Volunteer v JOIN Delivery_Assignment da ON v.Volunteer_ID = da.Volunteer_ID JOIN Pickup_Request pr ON da.Pickup_ID = pr.Pickup_ID JOIN Receiver r ON pr.Receiver_ID = r.Receiver_ID WHERE da.Delivery_Status = 'Delivered';",
            "SELECT d.Name, d.Rating, COUNT(f.Food_ID) AS TotalDonations FROM Donor d JOIN Food_Item f ON d.Donor_ID = f.Donor_ID WHERE d.Rating > 4.0 GROUP BY d.Donor_ID, d.Name, d.Rating ORDER BY TotalDonations DESC;",
            "SELECT Food_Name, Category, Quantity, Expiry_Date FROM Food_Item WHERE Expiry_Date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY) AND Status = 'Available' ORDER BY Expiry_Date ASC;"
    };

    public ReportsPanel() {
        setLayout(new BorderLayout(10, 10));
        setOpaque(false);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(GuiUtils.BORDER_COLOR), "Run Database Reports", 0, 0, GuiUtils.LABEL_FONT, GuiUtils.TEXT_COLOR));

        reportComboBox = GuiUtils.createStyledComboBox(reportNames);
        JButton runButton = GuiUtils.createStyledButton("Run Selected Report", GuiUtils.ACCENT_COLOR, "report");

        JPanel selectionPanel = new JPanel(new BorderLayout(10,0));
        selectionPanel.setOpaque(false);
        JLabel selectLabel = new JLabel("Select a Report:");
        selectLabel.setForeground(GuiUtils.TEXT_COLOR);
        selectionPanel.add(selectLabel, BorderLayout.WEST);
        selectionPanel.add(reportComboBox, BorderLayout.CENTER);
        selectionPanel.add(runButton, BorderLayout.EAST);

        queryTextArea = new JTextArea(3, 50);
        queryTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        queryTextArea.setEditable(false);
        queryTextArea.setLineWrap(true);
        queryTextArea.setWrapStyleWord(true);
        queryTextArea.setBorder(new EmptyBorder(5,5,5,5));

        topPanel.add(selectionPanel, BorderLayout.NORTH);
        topPanel.add(new JScrollPane(queryTextArea), BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        resultTable = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        GuiUtils.styleTable(resultTable);
        add(new JScrollPane(resultTable), BorderLayout.CENTER);

        reportComboBox.addActionListener(e -> {
            int selectedIndex = reportComboBox.getSelectedIndex();
            queryTextArea.setText(queries[selectedIndex]);
        });

        runButton.addActionListener(e -> executeQuery());

        queryTextArea.setText(queries[0]);
    }

    private void executeQuery() {
        int selectedIndex = reportComboBox.getSelectedIndex();
        String sql = queries[selectedIndex];

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            resultTable.setModel(GuiUtils.buildTableModel(rs));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error executing query: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
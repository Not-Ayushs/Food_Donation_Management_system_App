package org.example;
// Add these imports at the top of your GuiUtils.java file
import org.imgscalr.Scalr;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Vector;

public class GuiUtils {

    public static final Color PRIMARY_COLOR = new Color(0x1F2937);
    public static final Color SECONDARY_COLOR = new Color(0x111827);
    public static final Color ACCENT_COLOR = new Color(0x14B8A6);
    public static final Color TEXT_COLOR = new Color(0xD1D5DB);
    public static final Color BORDER_COLOR = new Color(0x374151);
    public static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);

    public static class PlaceholderTextField extends JTextField {
        private String placeholder;
        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
            setFont(INPUT_FONT);
            setForeground(Color.GRAY);
            setText(placeholder);
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (getText().equals(placeholder)) {
                        setText("");
                        setForeground(TEXT_COLOR);
                    }
                }
                @Override
                public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()) {
                        setForeground(Color.GRAY);
                        setText(placeholder);
                    }
                }
            });
        }
    }

    public static class RoundedPanel extends JPanel {
        private int cornerRadius;
        public RoundedPanel(int radius) {
            super();
            this.cornerRadius = radius;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
        }
    }

    public static JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(INPUT_FONT);
        return comboBox;
    }

    public static JButton createStyledButton(String text, Color backgroundColor, String iconName) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(backgroundColor);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if (iconName != null) {
            button.setIcon(getIcon(iconName, 16));
        }
        button.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { button.setBackground(backgroundColor.darker()); }
            public void mouseReleased(MouseEvent e) { button.setBackground(backgroundColor.brighter()); }
            public void mouseEntered(MouseEvent e) { button.setBackground(backgroundColor.brighter()); }
            public void mouseExited(MouseEvent e) { button.setBackground(backgroundColor); }
        });
        return button;
    }

    public static void styleTable(JTable table) {
        table.setFont(INPUT_FONT);
        table.setForeground(TEXT_COLOR);
        table.setRowHeight(35);
        table.setGridColor(BORDER_COLOR);
        table.setShowGrid(false);
        table.setShowVerticalLines(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(ACCENT_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setFont(LABEL_FONT);
        table.getTableHeader().setForeground(TEXT_COLOR);
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoCreateRowSorter(true);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private final Color oddColor = new Color(0x1A202C);
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? SECONDARY_COLOR : oddColor);
                }
                ((JLabel) c).setBorder(new EmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
    }

    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column).replace('_', ' '));
        }
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }
        return new DefaultTableModel(data, columnNames);
    }



// ... other existing methods ...

    public static ImageIcon getIcon(String name, int size) {
        try {
            // Load the original image using ImageIO for better compatibility
            BufferedImage originalImage = ImageIO.read(Objects.requireNonNull(GuiUtils.class.getResource("/icons/" + name + ".png")));

            // Use imgscalr to resize the image with the highest quality setting
            BufferedImage resizedImage = Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.AUTOMATIC, size, size, Scalr.OP_ANTIALIAS);

            return new ImageIcon(resizedImage);

        } catch (Exception e) {
            // If an error occurs, print it for debugging and return a blank icon
            e.printStackTrace();
            return new ImageIcon(new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB));
        }
    }
}
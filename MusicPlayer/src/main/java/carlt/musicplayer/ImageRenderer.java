/*
 * Click nbfs:// nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package carlt.musicplayer;

import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Carlt
 */

// Extends the DefaultTableCellRenderer class.
public class ImageRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = 1L;
    // Method customises the rendering of cells in a JTable.
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Creates a JLabel as the cell renderer.
        JLabel label = new JLabel();
        // Checks and loads image.
        if (value != null && value instanceof String) {
            ImageIcon originalIcon = new ImageIcon((String) value);
            label.setIcon(originalIcon);
            // The image icon is resized.
            Image image = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            ImageIcon resizedImage = new ImageIcon(image);
            
            label.setIcon(resizedImage);
        }
        // Centers image.
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }
}

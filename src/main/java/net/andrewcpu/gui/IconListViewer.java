package net.andrewcpu.gui;

import net.andrewcpu.gui.state.BehaviorState;
import net.andrewcpu.model.SelectionListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.Vector;

public class IconListViewer extends JPanel {

    public static JPanel iconViewerPanel(Vector<IconItem> items, SelectionListener selectionListener, SelectionListener onDuplicate, Runnable onNew) {
        // Create the JList for displaying IconItems
        JList<IconItem> list = new JList<>(items);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(1);
        list.setFixedCellHeight(80);
        list.setFixedCellWidth(80);
        list.setCellRenderer(new IconListRenderer());
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    IconItem selectedItem = list.getSelectedValue();
                    if (selectedItem != null) {
                        selectionListener.selected(selectedItem.getFilePath());
                    }
                }
            }
        });

        // Create the JScrollPane that wraps the JList
        JScrollPane scrollPane = new JScrollPane(list);

        // Create the JMenuBar and add buttons
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newButton = new JMenuItem("New");
        JMenuItem duplicateButton = new JMenuItem("Duplicate");

        // Add action listeners for the menu items
        newButton.addActionListener(e -> {
            // Implement what should happen when "New" is clicked
        });

        duplicateButton.addActionListener(e -> {
            // Implement what should happen when "Duplicate" is clicked
            if (list.getSelectedValue() != null) {
                // Assuming there's a way to duplicate the items in your application context
                onDuplicate.selected(list.getSelectedValue().filePath);
//                list.updateUI(); // Refresh the list to show the duplicated item
            }
        });

        fileMenu.add(newButton);
        fileMenu.add(duplicateButton);
        menuBar.add(fileMenu);

        // Create a panel to hold everything together
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(menuBar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
//            JFrame frame = new JFrame("Icon List Viewer");
//            items = new Vector<>();
//            items.add(new IconItem("Item 1", "path/to/icon1.png"));
//            items.add(new IconItem("Item 2", "path/to/icon2.png"));
//            items.add(new IconItem("Item 3", "path/to/icon3.png"));
//            items.add(new IconItem("Item 4", "path/to/icon3.png"));
//            items.add(new IconItem("Item 5", "path/to/icon3.png"));
//            items.add(new IconItem("Item 6", "path/to/icon3.png"));
//            items.add(new IconItem("Item 7", "path/to/icon3.png"));

        });
    }

    static class IconItem {
        private String name;
        private ImageIcon icon;
        private String filePath;

        public IconItem(String filePath) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
                BehaviorState state = (BehaviorState) in.readObject();
                this.name = state.getName();
            } catch (Exception e) {

            }
            this.filePath = filePath;
            String iconPath = "/cube.png";
            URL imgUrl = getClass().getResource(iconPath);
            if (imgUrl != null) {
                ImageIcon tempIcon = new ImageIcon(imgUrl);
                Image img = tempIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                this.icon = new ImageIcon(img);
            } else {
                System.err.println("Couldn't find file: " + iconPath);
                this.icon = new ImageIcon(); // Maybe load a default image or leave it blank
            }
        }

        public String getName() {
            return name;
        }

        public ImageIcon getIcon() {
            return icon;
        }

        public String getFilePath() {
            return filePath;
        }
    }

    static class IconListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            IconItem item = (IconItem) value;
            JLabel label = (JLabel) super.getListCellRendererComponent(list, item.getName(), index, isSelected, cellHasFocus);
            label.setIcon(item.getIcon());
            label.setHorizontalTextPosition(JLabel.CENTER);
            label.setVerticalTextPosition(JLabel.BOTTOM);
            if (isSelected) {
                label.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
            } else {
                label.setBorder(BorderFactory.createEmptyBorder());
            }
            return label;
        }
    }
}

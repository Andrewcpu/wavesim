package net.andrewcpu.gui;

import net.andrewcpu.model.SelectionListener;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;
import java.util.function.BiFunction;

public class IconViewerPanel {

    private JList<IconListViewer.IconItem> list;
    private DefaultListModel<IconListViewer.IconItem> model;
    private JScrollPane scrollPane;
    private JPanel panel;
    private BiFunction<String, DefaultListModel, IconListViewer.IconItem> duplicateCallback;
    public IconViewerPanel(Vector<IconListViewer.IconItem> items, SelectionListener selectionListener,
                           BiFunction<String, DefaultListModel, IconListViewer.IconItem> onDuplicate, Runnable onNew) {
        model = new DefaultListModel<>();
        items.forEach(model::addElement);

        // Create the JList with the model
        list = new JList<>(model);
        list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        list.setVisibleRowCount(1);
        list.setFixedCellHeight(80);
        list.setFixedCellWidth(80);
        list.setCellRenderer(new IconListViewer.IconListRenderer());
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && list.getSelectedValue() != null) {
                selectionListener.selected(list.getSelectedValue().getFilePath());
            }
        });

        // Create the JScrollPane that wraps the JList
        scrollPane = new JScrollPane(list);

        // Create the JMenuBar and add buttons
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem newButton = new JMenuItem("New");
        JMenuItem duplicateButton = new JMenuItem("Duplicate");

        // Add action listeners for the menu items
        newButton.addActionListener(e -> {
            onNew.run();  // Delegate the creation of a new icon to an external method
        });

        duplicateButton.addActionListener(e -> {
            if (list.getSelectedValue() != null) {
                model.addElement(onDuplicate.apply(list.getSelectedValue().getFilePath(), model)); // Add the duplicated item to the model
            }
        });

        fileMenu.add(newButton);
        fileMenu.add(duplicateButton);
        menuBar.add(fileMenu);

        // Create a panel to hold everything together
        panel = new JPanel(new BorderLayout());
        panel.add(menuBar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return panel;
    }

    public void addItem(IconListViewer.IconItem item) {
        model.addElement(item); // Add an item
    }

    public void removeItem(IconListViewer.IconItem item) {
        model.removeElement(item); // Remove an item
    }

    public void updateItem(IconListViewer.IconItem oldItem, IconListViewer.IconItem newItem) {
        int index = model.indexOf(oldItem);
        if (index != -1) {
            model.set(index, newItem); // Update the item in the model
        }
    }

    public DefaultListModel<IconListViewer.IconItem> getModel() {
        return model;
    }
}

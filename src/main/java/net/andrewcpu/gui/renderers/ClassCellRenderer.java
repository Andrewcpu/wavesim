package net.andrewcpu.gui.renderers;

import javax.swing.*;
import java.awt.*;

/**
 * This class is a custom cell renderer for rendering Class objects in a JList.
 * It extends DefaultListCellRenderer to provide the default rendering functionality
 * and overrides the getListCellRendererComponent method to customize the rendering for Class objects.
 */
public class ClassCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Class) {
            setText(((Class<?>) value).getSimpleName());
        }
        return this;
    }
}

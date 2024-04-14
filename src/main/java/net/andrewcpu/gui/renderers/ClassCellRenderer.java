package net.andrewcpu.gui.renderers;

import javax.swing.*;
import java.awt.*;

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

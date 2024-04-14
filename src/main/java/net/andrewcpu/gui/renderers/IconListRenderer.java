package net.andrewcpu.gui.renderers;

import net.andrewcpu.gui.IconItem;

import javax.swing.*;
import java.awt.*;

public class IconListRenderer extends DefaultListCellRenderer {
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
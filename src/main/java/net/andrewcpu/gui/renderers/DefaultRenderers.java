package net.andrewcpu.gui.renderers;

import net.andrewcpu.solids.Ether;

import java.awt.*;
public class DefaultRenderers {
    public static void render(Graphics g, int i, int j, int x, int y, int w, int h, Ether[][] pond, double value) {
        double color = value * 255.0;
        int red = 0, green = 0, blue = 0;
        int secondaryColor = (int) Math.min(Math.abs(color), 255);
        double colorValue = Math.abs(Math.sin(Math.min(Math.abs(color), 255) / 255.0 * Math.PI * 2.0) * 255.0);
        if (color < 0) {
            blue = secondaryColor;
            green = (int) colorValue;
        } else {
            blue = secondaryColor;
            red = (int) colorValue;
        }
        g.setColor(new Color(red, green, blue));
        g.fillRect(x, y, w, h);
    }
}

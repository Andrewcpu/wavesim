package net.andrewcpu.gui.renderers;

import net.andrewcpu.solids.Ether;

import java.awt.*;
public class DefaultRenderers {
    public static Color[] positiveGradientColors = {
            new Color(0, 255, 255), // Bright Cyan
            new Color(0, 255, 0),   // Green
            new Color(255, 255, 0), // Yellow
            new Color(255, 128, 0), // Orange
            new Color(255, 0, 0)    // Red
    };

    public static Color[] negativeGradientColors = {
            new Color(0, 0, 128),   // Deep Blue
            new Color(0, 0, 255),   // Blue
            new Color(0, 128, 255), // Cyan
            new Color(128, 0, 255), // Purple
            new Color(255, 0, 255)  // Magenta
    };

//    public static void render(Graphics g, int i, int j, int x, int y, int w, int h, Ether[][] pond, double value) {
//        int clamp = 1;
//        double velocity = pond[i][j].getVelocity();
//        double clampedValue = Math.max(-clamp, Math.min(clamp, value));
//        double normalizedValue = (clampedValue + clamp) / (2.0 * clamp);
//
//        Color[] gradientColors;
//        if (velocity >= 0) {
//            gradientColors = positiveGradientColors;
//        } else {
//            gradientColors = negativeGradientColors;
//        }
//
//        int colorIndex = (int) (normalizedValue * (gradientColors.length - 1));
//        Color color = gradientColors[colorIndex];
//
//        g.setColor(color);
//        g.fillRect(x, y, w, h);
//    }
    public static void render(Graphics g, int i, int j, int x, int y, int w, int h, Ether[][] pond, double value) {
        double color = value * 255.0;
        int red = 0, green = 0, blue = 0;
        int secondaryColor = (int) Math.min(Math.abs(color), 255);
        double colorValue = Math.abs(Math.sin(Math.min(Math.abs(color), 255) / 255.0 * Math.PI * 2.0) * 255.0);
        if (color < 0) {
            blue = secondaryColor;
            green = (int) colorValue;
        } else {
            green = secondaryColor;
            red = (int) colorValue;
        }
        g.setColor(new Color(red, green, blue));
        g.fillRect(x, y, w, h);
    }
}

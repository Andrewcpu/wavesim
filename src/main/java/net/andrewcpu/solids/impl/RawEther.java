package net.andrewcpu.solids.impl;

import net.andrewcpu.solids.Ether;
import net.andrewcpu.solids.behaviors.impl.DefaultBehavior;

import java.awt.*;
import java.util.Random;


public class RawEther extends Ether {
    public RawEther() {
        super(new DefaultBehavior());
    }

    @Override
    public void render(Graphics g, int i, int j, int x, int y, int w, int h, Ether[][] pond, double value) {
        double color = value * 255.0;
        int red = 0, green = 0, blue = 0;
        int secondaryColor = (int) Math.min(Math.abs(color), 255);
        double colorValue = Math.abs(Math.sin(Math.min(Math.abs(color), 255) / 255.0 * Math.PI * 2.0) * 255.0);
        blue = secondaryColor;
        if (color < 0) {
            green = (int) colorValue;
        } else {
            red = (int) colorValue;
        }
        g.setColor(new Color(red, green, blue));
        g.fillRect(x, y, w, h);
    }
}

package net.andrewcpu.solids.impl;

import net.andrewcpu.solids.Ether;
import net.andrewcpu.solids.behaviors.impl.ChargedSolidBehavior;

import java.awt.*;
import java.util.List;

public class Wormhole extends Ether {
    private double charge;

    public Wormhole(double charge) {
        super(new ChargedSolidBehavior(charge));
        this.charge = charge;
    }

    public double getCharge() {
        return charge;
    }

    @Override
    public void render(Graphics g, int i, int j, int x, int y, int w, int h, Ether[][] pond, double value) {
        // Default color is black
        // Change the color based on the charge if adjacent to another wormhole
        if (charge > 0) {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLUE);
        }
        // Draw the border of the wormhole
        g.fillRect(x, y, w, h);
    }
}

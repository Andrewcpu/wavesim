package net.andrewcpu.solids.impl;

import net.andrewcpu.solids.Ether;
import net.andrewcpu.solids.behaviors.SolidBehavior;
import net.andrewcpu.solids.behaviors.impl.BlockingSolid;

import java.awt.*;

public class SolidWall extends Ether {
    public SolidWall() {
        super(new BlockingSolid());
    }

    @Override
    public void render(Graphics g, int i, int j, int x, int y, int w, int h, Ether[][] pond, double value) {
        g.setColor(Color.white);
        g.fillRect(x, y, w, h);
    }
}

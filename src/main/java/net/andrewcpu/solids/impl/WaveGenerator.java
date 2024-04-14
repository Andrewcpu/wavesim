package net.andrewcpu.solids.impl;

import net.andrewcpu.solids.Ether;
import net.andrewcpu.solids.behaviors.SolidBehavior;
import net.andrewcpu.solids.behaviors.impl.WaveGeneratorBehavior;

import java.awt.*;

public class WaveGenerator extends Ether {

    public WaveGenerator() {
        super(new WaveGeneratorBehavior(100.0, 500));
    }

    @Override
    public void render(Graphics g, int i, int j, int x, int y, int w, int h, Ether[][] pond, double value) {
        g.setColor(Color.cyan);
        g.fillRect(x,y,w,h);
    }
}

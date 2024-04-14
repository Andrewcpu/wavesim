package net.andrewcpu.solids.impl;

import net.andrewcpu.gui.renderers.DefaultRenderers;
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
        DefaultRenderers.render(g, i, j, x, y, w, h, pond, value);
    }
}

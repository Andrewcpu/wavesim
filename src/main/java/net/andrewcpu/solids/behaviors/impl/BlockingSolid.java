package net.andrewcpu.solids.impl;

import net.andrewcpu.solids.SolidBehavior;

public class BlockingSolid implements SolidBehavior {
    public void interact(double[][] pond, double[][] velocity, int i, int j, double[][] newPond, double[][] newVelocity) {
        newPond[i][j] = 0;
        newVelocity[i][j] = 0;
    }
}
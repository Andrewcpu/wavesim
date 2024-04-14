package net.andrewcpu.solids;

public interface SolidBehavior {
    void interact(double[][] pond, double[][] velocity, int i, int j, double[][] newPond, double[][] newVelocity);
}

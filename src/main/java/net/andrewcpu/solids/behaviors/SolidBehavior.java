package net.andrewcpu.solids.behaviors;

import net.andrewcpu.solids.Ether;

public interface SolidBehavior {
    void step();
    ForceRecord interact(Ether[][] grid, int i, int j);
    boolean isPermeable();
}

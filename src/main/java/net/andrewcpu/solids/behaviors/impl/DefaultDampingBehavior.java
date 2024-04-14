package net.andrewcpu.solids.behaviors.impl;

import static net.andrewcpu.Pond.DAMPING;
import static net.andrewcpu.Pond.VELOCITY_DAMPING;

public class DefaultDampingBehavior extends PermeableSolid {
    public DefaultDampingBehavior(double permeability) {
        super(permeability, VELOCITY_DAMPING, DAMPING);
    }
}

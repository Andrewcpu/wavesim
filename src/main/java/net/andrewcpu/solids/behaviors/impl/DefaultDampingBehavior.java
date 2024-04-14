package net.andrewcpu.solids.behaviors.impl;

import static net.andrewcpu.Pond.DAMPING;
import static net.andrewcpu.Pond.VELOCITY_DAMPING;

public class DefaultPermeabilityBehavior extends PermeableSolid {
    public DefaultPermeabilityBehavior(double permeability) {
        super(permeability, VELOCITY_DAMPING, DAMPING);
    }
}

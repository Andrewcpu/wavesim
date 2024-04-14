package net.andrewcpu.solids.behaviors.impl;

public class DefaultBehavior extends DefaultDampingBehavior {
    public DefaultBehavior() {
        super(1.0);
    }

    public DefaultBehavior(double permeability) {
        super(permeability);
    }
}

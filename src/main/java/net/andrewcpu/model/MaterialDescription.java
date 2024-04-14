package net.andrewcpu.model;

import net.andrewcpu.solids.behaviors.SolidBehavior;

import java.util.Map;

public class MaterialDescription {
    private double permeability;
    private double velocityDamping;
    private double damping;
    private double boundaryDamping;
    private Class<? extends SolidBehavior> behavior;
//    private Map<String, >
}

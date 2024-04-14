package net.andrewcpu.solids.impl;

import net.andrewcpu.solids.SolidBehavior;

public class PermeableSolid implements SolidBehavior {
    private double permeability;
    private double velocityDamping;
    private double damping;

    public PermeableSolid(double permeability, double velocityDamping, double damping) {
        this.permeability = permeability;
        this.velocityDamping = velocityDamping;
        this.damping = damping;
    }

    public void interact(double[][] pond, double[][] velocity, int i, int j, double[][] newPond, double[][] newVelocity) {
        // Example: simple model where permeability affects the wave propagation
        double laplacian = pond[i-1][j] + pond[i+1][j] + pond[i][j-1] + pond[i][j+1] - 4 * pond[i][j];
        newVelocity[i][j] = velocity[i][j] * (1 - this.velocityDamping) + laplacian * this.damping * this.permeability;
        newPond[i][j] = pond[i][j] + newVelocity[i][j] * permeability;
    }
}
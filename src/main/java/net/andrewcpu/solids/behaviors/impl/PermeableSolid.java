package net.andrewcpu.solids.behaviors.impl;

import net.andrewcpu.solids.Ether;
import net.andrewcpu.solids.behaviors.ForceRecord;
import net.andrewcpu.solids.behaviors.SolidBehavior;

public class PermeableSolid implements SolidBehavior {
    private double permeability;
    private double velocityDamping;
    private double damping;

    public PermeableSolid(double permeability, double velocityDamping, double damping) {
        this.permeability = permeability;
        this.velocityDamping = velocityDamping;
        this.damping = damping;
    }

    @Override
    public void step() {

    }

    public ForceRecord interact(Ether[][] grid, int i, int j) {
        int rows = grid.length;
        int cols = grid[0].length;
        double laplacian = -6 * grid[i][j].getValue();

        // Add orthogonal neighbors with checks for boundaries
        laplacian += (i > 0 ? grid[i - 1][j].getValue() : 0);
        laplacian += (i < rows - 1 ? grid[i + 1][j].getValue() : 0);
        laplacian += (j > 0 ? grid[i][j - 1].getValue() : 0);
        laplacian += (j < cols - 1 ? grid[i][j + 1].getValue() : 0);

        // Diagonal neighbors with damping at edges
        double boundaryDamping = 0.5; // Damping factor for edge cells
        laplacian += (i > 0 && j > 0 ? 0.5 * grid[i - 1][j - 1].getValue() : boundaryDamping * grid[i][j].getValue());
        laplacian += (i > 0 && j < cols - 1 ? 0.5 * grid[i - 1][j + 1].getValue() : boundaryDamping * grid[i][j].getValue());
        laplacian += (i < rows - 1 && j > 0 ? 0.5 * grid[i + 1][j - 1].getValue() : boundaryDamping * grid[i][j].getValue());
        laplacian += (i < rows - 1 && j < cols - 1 ? 0.5 * grid[i + 1][j + 1].getValue() : boundaryDamping * grid[i][j].getValue());

        double deltaVelocity = (laplacian * this.damping * this.permeability) - (grid[i][j].getVelocity() * this.velocityDamping);
        double deltaValue = deltaVelocity;

        return new ForceRecord(deltaValue, deltaVelocity);
    }

    @Override
    public boolean isPermeable() {
        return permeability > 0.000001;
    }
}

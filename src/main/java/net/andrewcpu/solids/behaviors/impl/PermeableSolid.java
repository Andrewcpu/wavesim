package net.andrewcpu.solids.behaviors.impl;

import net.andrewcpu.solids.Ether;
import net.andrewcpu.model.ForceRecord;
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
        double smoothingFactor = 0.5; // Adjust this value as needed

        // Add orthogonal neighbors with checks for boundaries and permeability
        laplacian += (i > 0 && grid[i - 1][j].getSolidBehavior().isPermeable() ? grid[i - 1][j].getValue() * smoothPermeability(this.permeability, ((PermeableSolid) grid[i - 1][j].getSolidBehavior()).getPermeability(), smoothingFactor) : 0);
        laplacian += (i < rows - 1 && grid[i + 1][j].getSolidBehavior().isPermeable() ? grid[i + 1][j].getValue() * smoothPermeability(this.permeability, ((PermeableSolid) grid[i + 1][j].getSolidBehavior()).getPermeability(), smoothingFactor) : 0);
        laplacian += (j > 0 && grid[i][j - 1].getSolidBehavior().isPermeable() ? grid[i][j - 1].getValue() * smoothPermeability(this.permeability, ((PermeableSolid) grid[i][j - 1].getSolidBehavior()).getPermeability(), smoothingFactor) : 0);
        laplacian += (j < cols - 1 && grid[i][j + 1].getSolidBehavior().isPermeable() ? grid[i][j + 1].getValue() * smoothPermeability(this.permeability, ((PermeableSolid) grid[i][j + 1].getSolidBehavior()).getPermeability(), smoothingFactor) : 0);

        // Diagonal neighbors with damping at edges and permeability
        double boundaryDamping = 0.5; // Damping factor for edge cells
        laplacian += (i > 0 && j > 0 && grid[i - 1][j - 1].getSolidBehavior().isPermeable() ? 0.5 * grid[i - 1][j - 1].getValue() * smoothPermeability(this.permeability, ((PermeableSolid) grid[i - 1][j - 1].getSolidBehavior()).getPermeability(), smoothingFactor) : boundaryDamping * grid[i][j].getValue());
        laplacian += (i > 0 && j < cols - 1 && grid[i - 1][j + 1].getSolidBehavior().isPermeable() ? 0.5 * grid[i - 1][j + 1].getValue() * smoothPermeability(this.permeability, ((PermeableSolid) grid[i - 1][j + 1].getSolidBehavior()).getPermeability(), smoothingFactor) : boundaryDamping * grid[i][j].getValue());
        laplacian += (i < rows - 1 && j > 0 && grid[i + 1][j - 1].getSolidBehavior().isPermeable() ? 0.5 * grid[i + 1][j - 1].getValue() * smoothPermeability(this.permeability, ((PermeableSolid) grid[i + 1][j - 1].getSolidBehavior()).getPermeability(), smoothingFactor) : boundaryDamping * grid[i][j].getValue());
        laplacian += (i < rows - 1 && j < cols - 1 && grid[i + 1][j + 1].getSolidBehavior().isPermeable() ? 0.5 * grid[i + 1][j + 1].getValue() * smoothPermeability(this.permeability, ((PermeableSolid) grid[i + 1][j + 1].getSolidBehavior()).getPermeability(), smoothingFactor) : boundaryDamping * grid[i][j].getValue());

        double deltaVelocity = (laplacian * this.damping * this.permeability) - (grid[i][j].getVelocity() * this.velocityDamping);
        double deltaValue = deltaVelocity;

        return new ForceRecord(deltaVelocity, deltaValue);
    }

    private double smoothPermeability(double permeability1, double permeability2, double smoothingFactor) {
        return (permeability1 + permeability2) / 2 * smoothingFactor;
    }
    @Override
    public boolean isPermeable() {
        return permeability > 0.000001;
    }

    public double getPermeability() {
        return permeability;
    }

    public void setPermeability(double permeability) {
        this.permeability = permeability;
    }

    public double getVelocityDamping() {
        return velocityDamping;
    }

    public void setVelocityDamping(double velocityDamping) {
        this.velocityDamping = velocityDamping;
    }

    public double getDamping() {
        return damping;
    }

    public void setDamping(double damping) {
        this.damping = damping;
    }
}

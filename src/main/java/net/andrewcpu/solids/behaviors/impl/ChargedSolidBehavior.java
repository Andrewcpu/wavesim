package net.andrewcpu.solids.behaviors.impl;

import net.andrewcpu.solids.Ether;
import net.andrewcpu.solids.behaviors.ForceRecord;

import java.util.List;

import static net.andrewcpu.Pond.DAMPING;

public class ChargedSolidBehavior extends PermeableSolid {
    private List<Ether> oppositeChargedEthers; // List to store opposite charged Ethers
    private double charge; // Charge of the Ether this behavior is attached to

    public ChargedSolidBehavior(double charge) {
        super(1.0, 0.04, DAMPING);
        this.charge = charge;
//        this.oppositeChargedEthers = oppositeChargedEthers;
    }

    public void setOppositeChargedEthers(List<Ether> oppositeChargedEthers) {
        this.oppositeChargedEthers = oppositeChargedEthers;
    }

    @Override
    public ForceRecord interact(Ether[][] grid, int i, int j) {
        Ether ether = grid[i][j];
        int n = oppositeChargedEthers.size() ;
        ForceRecord forceRecord = super.interact(grid, i, j);
        double a = forceRecord.deltaForce() * charge / n * 0.9;
        double b = forceRecord.deltaVelocity() / n * charge * 0.9;
        for (Ether oppEther : oppositeChargedEthers) {
            oppEther.applyWave(a, b);
        }


        // Zero out the force after distribution
        return new ForceRecord(-ether.getVelocity(), -ether.getValue());
    }

    @Override
    public boolean isPermeable() {
        return true; // Assuming it is always permeable
    }
}

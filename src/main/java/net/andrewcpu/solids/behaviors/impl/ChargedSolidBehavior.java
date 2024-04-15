package net.andrewcpu.solids.behaviors.impl;

import net.andrewcpu.model.impl.ChargeEnum;
import net.andrewcpu.solids.Ether;
import net.andrewcpu.model.ForceRecord;

import java.util.ArrayList;
import java.util.List;

import static net.andrewcpu.Pond.DAMPING;

public class ChargedSolidBehavior extends PermeableSolid {
    private List<Ether> oppositeChargedEthers; // List to store opposite charged Ethers
    private double charge; // Charge of the Ether this behavior is attached to

    public ChargedSolidBehavior(ChargeEnum e) {
        super(0.9, 0.001, DAMPING);
        this.charge = e.getValue();
        this.oppositeChargedEthers = new ArrayList<>();
    }

    public void setOppositeChargedEthers(List<Ether> oppositeChargedEthers) {
        this.oppositeChargedEthers = oppositeChargedEthers;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    @Override
    public ForceRecord interact(Ether[][] grid, int i, int j) {
        Ether ether = grid[i][j];
        int n = oppositeChargedEthers.size() ;
        ForceRecord forceRecord = super.interact(grid, i, j);
        double a = forceRecord.deltaForce() / n * charge ;
        double b = forceRecord.deltaVelocity() / n * charge ;
        for (Ether oppEther : oppositeChargedEthers) {
            oppEther.applyWave(a, 0);
        }


        // Zero out the force after distribution
        return new ForceRecord(-ether.getVelocity(), -ether.getValue());
    }

    @Override
    public boolean isPermeable() {
        return true; // Assuming it is always permeable
    }
}

package net.andrewcpu.solids.behaviors.impl;

import net.andrewcpu.solids.Ether;
import net.andrewcpu.model.ForceRecord;

public class WaveGeneratorBehavior extends DefaultBehavior {
    private double waveIntensity;
    private int wavePeriod;
    private int stepCounter;

    public WaveGeneratorBehavior(double waveIntensity, int wavePeriod) {
        super(0.2);
        this.waveIntensity = waveIntensity;
        this.wavePeriod = wavePeriod;
        this.stepCounter = 0;
    }

    @Override
    public void step() {
        stepCounter++;
        if (stepCounter >= wavePeriod) {
            stepCounter = 0;
        }
    }

    @Override
    public ForceRecord interact(Ether[][] grid, int i, int j) {
        ForceRecord baseForce = super.interact(grid, i, j);

        if (stepCounter == 0) {
            double deltaVelocity = baseForce.deltaVelocity() + waveIntensity;
            double deltaValue = deltaVelocity;

            return new ForceRecord(deltaVelocity, deltaValue);
        }

        return baseForce;
    }
}
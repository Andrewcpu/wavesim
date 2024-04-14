package net.andrewcpu.solids;

import net.andrewcpu.model.ForceRecord;
import net.andrewcpu.solids.behaviors.SolidBehavior;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Ether class represents a generic ether particle in a simulation.
 * It contains properties and methods common to all ether particles.
 */
public abstract class Ether {
    private List<ForceRecord> forceRecords; // last force record
    private SolidBehavior solidBehavior;
    protected double velocity;
    protected double value;
    protected double forceDampening;
    protected double velocityDamping;

    public Ether(SolidBehavior solidBehavior) {
        this.forceRecords = new ArrayList<>();
        this.solidBehavior = solidBehavior;
        this.velocity = 0.0;
        this.value = 0.0;
        this.forceDampening = 1.0;
        this.velocityDamping = 1.0;
    }

    public SolidBehavior getSolidBehavior() {
        return solidBehavior;
    }

    public void interact(Ether[][] ether, int x, int y) {
        forceRecords.add(solidBehavior.interact(ether, x, y));
    }

    public abstract void render(Graphics g, int i, int j, int x, int y, int w, int h, Ether[][] pond, double value);

    public void applyForce(double force) {
        if(getSolidBehavior().isPermeable()) {
            this.value += (force * forceDampening);
        }
    }

    public void addForceRecord(ForceRecord forceRecord) {
        this.forceRecords.add(forceRecord);
    }
    public void applyWave(double force, double velocity) {
        if(getSolidBehavior().isPermeable()) {
            this.value += (force * forceDampening);
            this.velocity += (velocity * velocityDamping);
        }
    }

    /**
     * Update the velocity and value of the ether particle based on the accumulated force records.
     * If there are no force records, no update is performed.
     * The velocity is updated first by accumulating the delta velocities from all the force records.
     * Then, the value is updated based on the new velocity.
     * Finally, the force records are cleared.
     */
    public void commit() {
        if(this.forceRecords.isEmpty()) return;
        double vel = 0.0;
        for(ForceRecord forceRecord : forceRecords) {
            vel += forceRecord.deltaVelocity();
        }
        this.velocity += vel; // Update velocity first
        this.value += this.velocity; // Then update value based on new velocity
        this.value *= 0.999;
        this.velocity *= 0.999;
        forceRecords.clear();
    }


    public double getVelocity() {
        return velocity;
    }

    public double getValue() {
        return value;
    }

    public void setSolidBehavior(SolidBehavior solidBehavior) {
        this.solidBehavior = solidBehavior;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setValue(double value) {
        this.value = value;
    }
}

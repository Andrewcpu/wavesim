package net.andrewcpu.solids;

import net.andrewcpu.solids.behaviors.ForceRecord;
import net.andrewcpu.solids.behaviors.SolidBehavior;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
        this.value += (force * forceDampening);
        this.velocity += (velocity * velocityDamping);
    }

    public void commit() {
        if(this.forceRecords.isEmpty()) return;
        double vel = 0.0;
        for(ForceRecord forceRecord : forceRecords) {
            vel += forceRecord.deltaVelocity();
        }
        this.velocity += vel; // Update velocity first
        this.value += this.velocity; // Then update value based on new velocity
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

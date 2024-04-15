

package net.andrewcpu;

import net.andrewcpu.gui.panels.BehaviorPanel;
import net.andrewcpu.model.impl.ChargeEnum;
import net.andrewcpu.solids.Ether;
import net.andrewcpu.solids.behaviors.impl.ChargedSolidBehavior;
import net.andrewcpu.solids.impl.DynamicMaterial;
import net.andrewcpu.solids.impl.RawEther;
import net.andrewcpu.solids.impl.SolidWall;
import net.andrewcpu.solids.impl.Wormhole;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Pond {
    public static final Map<String, DynamicMaterial> dynamicRegistry = new HashMap<>();
    public static DynamicMaterial currentMaterial;
    public static BehaviorPanel behaviorPanel;
    private final List<Ether> positiveWormholes = new ArrayList<>();
    private final List<Ether> negativeWormholes = new ArrayList<>();

    public static final double DAMPING = 0.25;
    public static final double VELOCITY_DAMPING = 0.02;
    public static final int POND_SIZE = 150;
    public static final int TILE_SIZE = 1;
    private Ether[][] etherGrid = new Ether[POND_SIZE][POND_SIZE];
    private int cursorX = 0, cursorY = 0;
    private boolean positiveBang = true;
    private final int wormholeRadius = 10;

    public Pond() {
        // Initialize all cells with a fully permeable behavior by default
        List<Point> positiveHoles = new ArrayList<>();
        List<Point> negativeHoles = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            int posX = (int) (Math.random() * POND_SIZE);
            int posY = (int) (Math.random() * POND_SIZE);
            int negX = (int) (Math.random() * POND_SIZE);
            int negY = (int) (Math.random() * POND_SIZE);
            positiveHoles.add(new Point(posX, posY));
            negativeHoles.add(new Point(negX, negY));
        }


        double f = 1.25;
        for (int i = 0; i < POND_SIZE; i++) {
            for (int j = 0; j < POND_SIZE; j++) {
                double distPos = Double.MAX_VALUE;
                double distNeg = Double.MAX_VALUE;
                for (Point pos : positiveHoles) {
                    double d = pos.distance(i, j);
                    if (d < distPos) {
                        distPos = d;
                    }
                }
                for (Point neg : negativeHoles) {
                    double d = neg.distance(i, j);
                    if (d < distNeg) {
                        distNeg = d;
                    }
                }
                if (distPos < wormholeRadius) {
                    if (distPos >= wormholeRadius - f) {
                        set(i, j, new Wormhole(ChargeEnum.POSITIVE));
                    } else {
                        set(i, j, new SolidWall());
                    }
                } else if (distNeg < wormholeRadius) {
                    if (distNeg >= wormholeRadius - f) {
                        set(i, j, new Wormhole(ChargeEnum.NEGATIVE));
                    } else {
                        set(i, j, new SolidWall());
                    }
                } else {
                    set(i, j, new RawEther());
                }
            }
        }
    }


    public void set(int i, int j, Ether ether) {
        etherGrid[i][j] = ether;

        if (ether.getSolidBehavior() instanceof ChargedSolidBehavior chargedSolidBehavior) {
            if (chargedSolidBehavior.getCharge() > 0) {
                positiveWormholes.add(ether);
            } else {
                negativeWormholes.add(ether);
            }
            updateOppositeCharges();
        }
    }

    private void updateOppositeCharges() {
        for (Ether pos : positiveWormholes) {
            ((ChargedSolidBehavior) pos.getSolidBehavior()).setOppositeChargedEthers(negativeWormholes);
        }
        for (Ether neg : negativeWormholes) {
            ((ChargedSolidBehavior) neg.getSolidBehavior()).setOppositeChargedEthers(positiveWormholes);
        }
    }

    public Ether get(int i, int j) {
        return etherGrid[i][j];
    }


    private void reset() {
        positiveWormholes.clear();
        negativeWormholes.clear();
        for (int i = 0; i < POND_SIZE; i++) {
            for (int j = 0; j < POND_SIZE; j++) {
                if (get(i, j) instanceof RawEther) continue;
                set(i, j, new RawEther());
            }
        }
    }

    public void keyPress(int key) {
        switch (key) {
            case KeyEvent.VK_SPACE:
                System.out.println(cursorX + ", " + cursorY);
                applyForce(cursorX, cursorY, (positiveBang ? -1 : 1) * 100.0);
                break;
            case KeyEvent.VK_R:
                reset();
                break;
            case KeyEvent.VK_ENTER:
                positiveBang = !positiveBang;
                break;

        }
    }

    public static DynamicMaterial getCurrentMaterial() {
        return currentMaterial;
    }

    public static void setCurrentMaterial(DynamicMaterial currentMaterial) {
        Pond.currentMaterial = currentMaterial;
    }

    public void toggleSolidObject(int x, int y) {
//        if (get(x, y) instanceof RawEther) {
//            set(x, y, new SolidWall());
//        } else {
//            set(x, y, new RawEther());
//        }
        if(currentMaterial == null) return;
        set(x, y, currentMaterial);
        System.out.println(currentMaterial);
    }

    public void applyForce(int x, int y, double force) {
        if (x >= 0 && x < POND_SIZE && y >= 0 && y < POND_SIZE) {
            get(x, y).applyForce(force);
        }
    }
    private CompletableFuture[] futures = new CompletableFuture[POND_SIZE - 2];
    public void step() {

        for (int i = 1; i < POND_SIZE - 1; i++) {
            int finalI = i;
            futures[i - 1] = (CompletableFuture.runAsync(() -> {
                for (int j = 1; j < POND_SIZE - 1; j++) {
                    get(finalI, j).getSolidBehavior().step();
                    get(finalI, j).interact(etherGrid, finalI, j);
                }
            }));
        }
        CompletableFuture.allOf(futures).join();
        for (int i = 1; i < POND_SIZE - 1; i++) {
            for (int j = 1; j < POND_SIZE - 1; j++) {
                get(i, j).commit();
            }
        }
    }

    public BufferedImage render() {
        BufferedImage image = new BufferedImage(POND_SIZE * TILE_SIZE, POND_SIZE * TILE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();

        for (int i = 0; i < POND_SIZE; i++) {
            for (int j = 0; j < POND_SIZE; j++) {
                Ether ether = get(i, j);
                if(ether == null) continue;
                ether.render(graphics, i, j, i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE, etherGrid, ether.getValue());
            }
        }
        graphics.dispose();
        return image;
    }


    public void setCursorX(int cursorX) {
        this.cursorX = cursorX;
    }

    public void setCursorY(int cursorY) {
        this.cursorY = cursorY;
    }

    public int getCursorX() {
        return cursorX;
    }

    public int getCursorY() {
        return cursorY;
    }

    public void updateMaterial(UUID uuid) {
        for (int i = 0; i < POND_SIZE; i++) {
            for (int j = 0; j < POND_SIZE; j++) {
                if (get(i, j) instanceof DynamicMaterial dynamicMaterial){
                    if(dynamicMaterial.uuid == uuid) {
                        dynamicMaterial.commit();
//                        dynamicMaterial.setVelocity();
                        dynamicMaterial.setSolidBehavior(currentMaterial.getSolidBehavior());
                        dynamicMaterial.setRender(currentMaterial.render);
//                        dynamicMaterial.setValue(0);
//                        dynamicMaterial.setVelocity(0);
                    }
                }
            }
        }
    }
}

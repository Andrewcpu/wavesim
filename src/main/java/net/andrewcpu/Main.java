package net.andrewcpu;

import net.andrewcpu.gui.MainUI;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static net.andrewcpu.Pond.POND_SIZE;

public class Main extends JFrame {
    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        int dim = 800;
        SimComponent simComponent = new SimComponent();
        simComponent.setFocusable(true);
        simComponent.setPreferredSize(new Dimension(dim, dim)); // Define preferred size for layout management

        MainUI mainUI = new MainUI(simComponent.pond); // Create an instance of your new MainUI panel
        mainUI.setPreferredSize(new Dimension(400, dim)); // Optional: Set preferred size for MainUI

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, simComponent, mainUI);
        splitPane.setDividerLocation(dim); // Set the initial position of the divider
        splitPane.setResizeWeight(0.5); // This sets the resize behavior

        getContentPane().add(splitPane); // Add the split pane to the frame
        pack(); // Adjusts the frame size to fit the components
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);

        simComponent.addKeyListener(simComponent);
        simComponent.addMouseListener(simComponent);
        simComponent.addMouseWheelListener(simComponent);
        simComponent.addMouseMotionListener(simComponent);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                simComponent.pond.step();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 20, 20, TimeUnit.MILLISECONDS);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            int randX = (int)(Math.random() * (POND_SIZE - 2)) + 1;
            int randY = (int)(Math.random() * (POND_SIZE - 2)) + 1;
            simComponent.pond.applyForce(randX, randY, (Math.random() < 0.5 ? -1 : 1) * (int)(Math.random() * 500.0));
        }, 2, 2, TimeUnit.SECONDS);
    }
}

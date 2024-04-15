package net.andrewcpu;

import net.andrewcpu.gui.SidebarUI;
import net.andrewcpu.gui.SimulationComponent;

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
        SimulationComponent simulationComponent = new SimulationComponent();
        simulationComponent.setFocusable(true);
        simulationComponent.setPreferredSize(new Dimension(dim, dim)); // Define preferred size for layout management

        SidebarUI sidebarUI = new SidebarUI(simulationComponent.pond); // Create an instance of your new MainUI panel
        sidebarUI.setPreferredSize(new Dimension(400, dim)); // Optional: Set preferred size for MainUI
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, simulationComponent, sidebarUI);
        splitPane.setDividerLocation(dim); // Set the initial position of the divider
        splitPane.setResizeWeight(0.5); // This sets the resize behavior

        getContentPane().add(splitPane); // Add the split pane to the frame
        pack(); // Adjusts the frame size to fit the components
        setLocationRelativeTo(null); // Center the frame on the screen
        setVisible(true);
        sidebarUI.finishedLoading();

        simulationComponent.addKeyListener(simulationComponent);
        simulationComponent.addMouseListener(simulationComponent);
        simulationComponent.addMouseWheelListener(simulationComponent);
        simulationComponent.addMouseMotionListener(simulationComponent);

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                simulationComponent.pond.step();
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
            simulationComponent.pond.applyForce(randX, randY, (Math.random() < 0.5 ? -1 : 1) * (int)(Math.random() * 500.0));
        }, 2, 2, TimeUnit.SECONDS);
    }
}

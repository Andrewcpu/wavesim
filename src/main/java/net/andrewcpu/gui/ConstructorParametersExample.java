package net.andrewcpu.gui;

import net.andrewcpu.solids.behaviors.impl.BlockingSolid;
import net.andrewcpu.solids.behaviors.impl.PermeableSolid;
import net.andrewcpu.solids.behaviors.impl.WaveGeneratorBehavior;

import javax.swing.*;
import java.util.List;

public class ConstructorParametersExample extends JFrame {

    public ConstructorParametersExample() {
        setTitle("Constructor Parameters Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//        BehaviorPanel parameterPanel = new BehaviorPanel(List.of(BlockingSolid.class, PermeableSolid.class, WaveGeneratorBehavior.class));
//
//        add(parameterPanel);
//
//        pack();
//        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ConstructorParametersExample::new);
    }
}
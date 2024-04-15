package net.andrewcpu.gui;

import net.andrewcpu.Pond;
import net.andrewcpu.gui.panels.BehaviorPanel;
import net.andrewcpu.gui.panels.IconViewerPanel;
import net.andrewcpu.gui.state.BehaviorState;
import net.andrewcpu.model.SelectionListener;
import net.andrewcpu.solids.behaviors.impl.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class SidebarUI extends JPanel implements SelectionListener {
    private BehaviorPanel behaviorPanel;
    private File saveDirectory = new File("saved");
    private String lastSelected;
    private Pond pond;

    public SidebarUI(Pond pond) {
        saveDirectory.mkdirs();
        this.pond = pond;
        setLayout(new BorderLayout());

        File[] files = saveDirectory.listFiles();
        Vector<IconItem> items = Arrays.stream(files)
                .map(e -> new IconItem(e.getAbsolutePath()))
                .collect(Collectors.toCollection(Vector<IconItem>::new));

        IconViewerPanel iconScrollPane = new IconViewerPanel(items, this, (duplicate, model) -> {
            File file = new File(duplicate);
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                BehaviorState state = (BehaviorState) in.readObject();
                state.setName("Copy of " + state.getName());
                state.setUuid(UUID.randomUUID());
                File newFile = new File(file.getParentFile(), state.getUuid().toString() + ".ser");
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(newFile))) {
                    out.writeObject(state); // Write the modified object back to the file
                } catch (IOException e) {
                    System.err.println("Error writing the object: " + e.getMessage());
                }
                return new IconItem(newFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }, () -> {
            behaviorPanel.resetPanel();
        });

        behaviorPanel = new BehaviorPanel(loadBehaviorClasses(), saveDirectory, () -> {
            try {
                lastSelected = new File(saveDirectory, behaviorPanel.currentUUID.toString() + ".ser").getAbsolutePath();
                behaviorPanel.loadState(new File(lastSelected));
                pond.currentMaterial = behaviorPanel.build();
                pond.updateMaterial(pond.currentMaterial.uuid);
                File[] updatedFiles = saveDirectory.listFiles();
                Vector<IconItem> updatedItems = Arrays.stream(updatedFiles)
                        .map(e -> new IconItem(e.getAbsolutePath()))
                        .collect(Collectors.toCollection(Vector<IconItem>::new));
                iconScrollPane.getModel().clear();
                iconScrollPane.getModel().addAll(updatedItems);
            } catch (Exception e) {
                e.printStackTrace();
//                throw new RuntimeException(e);
            }
        });
        JScrollPane behaviorScrollPane = new JScrollPane(behaviorPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, iconScrollPane.getPanel(), behaviorScrollPane);
        splitPane.setDividerLocation(150);

        add(splitPane, BorderLayout.CENTER);
    }

    public void finishedLoading() {
        behaviorPanel.updateParameterPanel();
    }

    private List<Class<?>> loadBehaviorClasses() {
        List<Class<?>> classes = new ArrayList<>();
        classes.add(BlockingSolid.class);
        classes.add(PermeableSolid.class);
        classes.add(DefaultDampingBehavior.class);
        classes.add(WaveGeneratorBehavior.class);
        classes.add(DefaultBehavior.class);
        classes.add(ChargedSolidBehavior.class);
        return classes;
    }

    @Override
    public void selected(String filePath) {
        try {
            lastSelected = filePath;
            behaviorPanel.loadState(new File(filePath));
            pond.currentMaterial = behaviorPanel.build();
            pond.behaviorPanel = behaviorPanel;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

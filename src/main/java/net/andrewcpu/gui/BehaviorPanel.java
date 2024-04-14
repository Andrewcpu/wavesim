package net.andrewcpu.gui;

import net.andrewcpu.Pond;
import net.andrewcpu.gui.renderers.ClassCellRenderer;
import net.andrewcpu.gui.state.BehaviorState;
import net.andrewcpu.model.DynamicConstructionDescription;
import net.andrewcpu.solids.behaviors.SolidBehavior;
import net.andrewcpu.solids.behaviors.impl.BlockingSolid;
import net.andrewcpu.solids.behaviors.impl.PermeableSolid;
import net.andrewcpu.solids.behaviors.impl.WaveGeneratorBehavior;
import net.andrewcpu.solids.impl.DynamicMaterial;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BehaviorPanel extends JPanel {
    private File saveDirectory;
    private Runnable onSave;
    private List<Class<?>> behaviorClasses;
    public UUID currentUUID;
    private JTextField nameField;
    private JComboBox<Class<?>> behaviorDropdown;
    private JTextArea textArea;
    private JPanel parameterPanel;

    private DynamicConstructionDescription<?> behaviorDescription;

    public BehaviorPanel(List<Class<?>> behaviorClasses, File saveDirectory, Runnable onSave) {
        this.behaviorClasses = behaviorClasses;
        this.saveDirectory = saveDirectory;
        this.onSave = onSave;
        setLayout(new BorderLayout());
        buildPanel();
    }

    private void buildPanel() {
        removeAll(); // Clear existing components
        currentUUID = UUID.randomUUID();

        // Top Panel with name and behavior dropdown
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        JPanel namePanel = buildNamePanel();
        JPanel behaviorPanel = buildBehaviorPanel();

        topPanel.add(namePanel);
        topPanel.add(behaviorPanel);
        add(topPanel, BorderLayout.NORTH);

        // Center parameter panel
        parameterPanel = new JPanel(new BorderLayout());
        add(parameterPanel, BorderLayout.CENTER);

        // South text area and save button
        JPanel southPanel = buildSouthPanel();
        add(southPanel, BorderLayout.SOUTH);

        validate();
        repaint();
    }

    private JPanel buildNamePanel() {
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        nameField = new JTextField(20);
        nameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        return namePanel;
    }

    private JPanel buildBehaviorPanel() {
        JPanel behaviorPanel = new JPanel();
        behaviorPanel.setLayout(new BoxLayout(behaviorPanel, BoxLayout.Y_AXIS));
        JLabel behaviorLabel = new JLabel("Behavior:");
        behaviorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        behaviorDropdown = new JComboBox<>(behaviorClasses.toArray(new Class<?>[0]));
        behaviorDropdown.setAlignmentX(Component.LEFT_ALIGNMENT);
        behaviorDropdown.addActionListener(e -> updateParameterPanel());
        behaviorDropdown.setRenderer(new ClassCellRenderer());  // Assuming ClassCellRenderer is defined elsewhere
        behaviorPanel.add(behaviorLabel);
        behaviorPanel.add(behaviorDropdown);
        return behaviorPanel;
    }

    private JPanel buildSouthPanel() {
        JPanel southPanel = new JPanel(new BorderLayout());
        textArea = new JTextArea(5, 20);
        textArea.setText("DefaultRenderers.render(g, i, j, x, y, w, h, pond, value);");
        textArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        JScrollPane scrollPane = new JScrollPane(textArea);
        southPanel.add(scrollPane, BorderLayout.CENTER);
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                File file = new File(saveDirectory, currentUUID + ".ser");
                saveState(file);
                onSave.run();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        southPanel.add(saveButton, BorderLayout.PAGE_END);
        return southPanel;
    }

    public DynamicMaterial build() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        DynamicMaterial dynamicMaterial;
        if(Pond.dynamicRegistry.containsKey(currentUUID.toString())){
            dynamicMaterial = Pond.dynamicRegistry.get(currentUUID.toString());
            dynamicMaterial.setRender(textArea.getText());
            dynamicMaterial.setSolidBehavior((SolidBehavior) behaviorDescription.build());
        } else {
            dynamicMaterial = new DynamicMaterial(currentUUID, (SolidBehavior) behaviorDescription.build(), textArea.getText());
            Pond.dynamicRegistry.put(currentUUID.toString(), dynamicMaterial);
        }
        DynamicMaterial dynamicMaterial1 = new DynamicMaterial(dynamicMaterial.uuid, dynamicMaterial.getSolidBehavior(), dynamicMaterial.render);
        return dynamicMaterial1;
    }

    public void resetPanel() {
        buildPanel(); // Rebuild the panel to reset
    }

    private void updateParameterPanel() {
        parameterPanel.removeAll(); // Remove all components

        Class<?> selectedClass = (Class<?>) behaviorDropdown.getSelectedItem();
        if (selectedClass != null) {
            ParameterPanel panel = new ParameterPanel(selectedClass, this::handleBehaviorDescription);
            parameterPanel.add(panel, BorderLayout.CENTER); // Ensure this is correctly using BorderLayout
        }

//        parameterPanel.revalidate(); // Recalculate layout
//        parameterPanel.repaint(); // Redraw the panel
        revalidate();
        repaint();

        // Call pack() on the top-level window
        SwingUtilities.getWindowAncestor(this).pack();
    }
    private void handleBehaviorDescription(DynamicConstructionDescription<?> description) {
        // Handle the saved description, if needed
        this.behaviorDescription = description;
    }


    public String getName() {
        return nameField.getText();
    }

    public Class<?> getSelectedBehaviorClass() {
        return (Class<?>) behaviorDropdown.getSelectedItem();
    }

    public ParameterPanel getParameterPanel() {
        Component[] components = parameterPanel.getComponents();
        if (components.length > 0 && components[0] instanceof ParameterPanel) {
            return (ParameterPanel) components[0];
        }
        return null;
    }

    public String getTextAreaContent() {
        return textArea.getText();
    }



    public void saveState(File file) throws IOException {
        BehaviorState state = new BehaviorState(getName(), getSelectedBehaviorClass(), behaviorDescription, currentUUID, textArea.getText());
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(state);
        }
    }

    public void loadState(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            BehaviorState state = (BehaviorState) in.readObject();
            currentUUID = state.getUuid(); // Set the loaded UUID
            nameField.setText(state.getName());
            textArea.setText(state.getRenderCode());
            behaviorDropdown.setSelectedItem(state.getBehaviorClass());
            updateParameterPanelWithState(state.getParameters());
        }
    }
    private void updateParameterPanelWithState(DynamicConstructionDescription<?> params) {
        // Implement this method to update the UI based on the loaded parameters
        handleBehaviorDescription(params);
        ParameterPanel panel = new ParameterPanel((Class<?>) behaviorDropdown.getSelectedItem(), this::handleBehaviorDescription);
        panel.loadParameters(params); // Implement this method in ParameterPanel
        parameterPanel.removeAll();
        parameterPanel.add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}
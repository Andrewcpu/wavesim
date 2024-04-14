package net.andrewcpu.gui.panels;

import net.andrewcpu.model.DynamicConstructionDescription;
import net.andrewcpu.model.KeyPairEnum;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ParameterPanel extends JPanel {
    private Map<String, ParameterField> parameterFields;
    private Map<String, Object> savedValues;
    private JButton saveButton;
    private Consumer<DynamicConstructionDescription> saveCallback;
    private Class<?> clazz;

    public ParameterPanel(Class<?> clazz, Consumer<DynamicConstructionDescription> saveCallback) {
        parameterFields = new HashMap<>();
        this.clazz = clazz;
        setLayout(new GridBagLayout());
        this.saveCallback = saveCallback;

        Constructor<?>[] constructors = clazz.getConstructors();
        if (constructors.length > 0) {
            Constructor<?> constructor = constructors[0];
            Parameter[] parameters = constructor.getParameters();
            for (Parameter parameter : parameters) {
                addParameterField(parameter);
            }
        }
//        saveButton = new JButton("Save");
//        saveButton.addActionListener(e -> saveValues());
//
//        GridBagConstraints constraints = new GridBagConstraints();
//        constraints.gridx = 1;
//        constraints.gridy = parameterFields.size();
//        constraints.anchor = GridBagConstraints.EAST;
//        constraints.insets = new Insets(5, 5, 5, 5);
//        add(saveButton, constraints);
    }

    private void addParameterField(Parameter parameter) {
        String name = parameter.getName();
        Class<?> type = parameter.getType();
        Object defaultValue = getDefaultValue(type);

        ParameterField field = new ParameterField(name, type, defaultValue);
        parameterFields.put(name, field);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = parameterFields.size() - 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        add(field.getLabel(), constraints);

        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        add(field.getInputField(), constraints);
    }

    public void saveValues() {
        savedValues = new HashMap<>();
        for (Map.Entry<String, ParameterField> entry : parameterFields.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue().getValue();
            savedValues.put(name, value);
        }
        saveCallback.accept(new DynamicConstructionDescription(clazz, savedValues));
    }

    public Map<String, Object> getSavedValues() {
        return savedValues;
    }

    private Object getDefaultValue(Class<?> type) {
        if (type == int.class || type == Integer.class) {
            return 0;
        } else if (type == double.class || type == Double.class) {
            return 0.0;
        } else if (type.isEnum()) {
            return type.getEnumConstants()[0];
        }
        return null;
    }

    public Object getParameterValue(String name) {
        ParameterField field = parameterFields.get(name);
        if (field != null) {
            return field.getValue();
        }
        return null;
    }

    private static class ParameterField {
        private String name;
        private JLabel label;
        private JComponent inputField;

        public ParameterField(String name, Class<?> type, Object defaultValue) {
            this.name = name;
            this.label = new JLabel(name + ":");

            if (type == int.class || type == Integer.class) {
                SpinnerNumberModel model = new SpinnerNumberModel((int) defaultValue, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
                inputField = new JSpinner(model);
            } else if (type == double.class || type == Double.class) {
                SpinnerNumberModel model = new SpinnerNumberModel((double) defaultValue, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0.1);
                inputField = new JSpinner(model);
            } else if (type.isEnum()) {
                if (KeyPairEnum.class.isAssignableFrom(type)) {
                    KeyPairEnum<?, ?>[] enumConstants = (KeyPairEnum<?, ?>[]) type.getEnumConstants();
                    JComboBox<KeyPairEnum<?, ?>> comboBox = new JComboBox<>(enumConstants);
                    inputField = comboBox;
                } else {
                    inputField = new JComboBox<>(type.getEnumConstants());
                }
                ((JComboBox<?>) inputField).setSelectedItem(defaultValue);
            }
        }

        public String getName() {
            return name;
        }

        public JLabel getLabel() {
            return label;
        }

        public JComponent getInputField() {
            return inputField;
        }

        public Object getValue() {
            if (inputField instanceof JSpinner) {
                return ((JSpinner) inputField).getValue();
            } else if (inputField instanceof JComboBox) {
                return ((JComboBox<?>) inputField).getSelectedItem();
            }
            return null;
        }

        public void setValue(Object value) {
            if (inputField instanceof JSpinner) {
                ((JSpinner) inputField).setValue(value);
            } else if (inputField instanceof JComboBox) {
                JComboBox comboBox = (JComboBox) inputField;
                comboBox.setSelectedItem(value);
                // Ensure the item is in the list; if not, consider adding or handling it
                if (comboBox.getSelectedIndex() == -1) {
                    comboBox.addItem(value);
                    comboBox.setSelectedItem(value);
                }
            }
        }
    }
    public void loadParameters(DynamicConstructionDescription<?> params) {
        // Extract the parameter values from the DynamicConstructionDescription
        Map<String, Object> values = params.getConstructorParameters();

        // Iterate through the entries of the values map and update the corresponding ParameterField
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            ParameterField field = parameterFields.get(entry.getKey());
            if (field != null) {
                field.setValue(entry.getValue());
            }
        }
    }
}
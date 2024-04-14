package net.andrewcpu.solids.impl;

import net.andrewcpu.solids.Ether;
import net.andrewcpu.solids.behaviors.SolidBehavior;
import org.codehaus.janino.SimpleCompiler;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.UUID;

public class DynamicMaterial extends Ether {
    private static final HashMap<String, Object> compiledInstances = new HashMap<>(); // Store instances
    public UUID uuid;
    public String render;
    private Object dynamicInstance; // To store the specific instance for this object
    private Method dynamicMethod; // To store the specific method

    public DynamicMaterial(UUID uuid, SolidBehavior solidBehavior, String render) {
        super(solidBehavior);
        this.uuid = uuid;
        this.render = render;
        initializeDynamicInstance(); // Initialize the dynamic instance
    }

    public void setRender(String render) {
        this.render = render;
        initializeDynamicInstance(); // Re-initialize when render changes
    }

    private void initializeDynamicInstance() {
        try {
            if (!compiledInstances.containsKey(render)) {
                String classCode = generateClassCode(render);
                SimpleCompiler compiler = new SimpleCompiler();
                compiler.cook(classCode);
                Class<?> clazz = compiler.getClassLoader().loadClass("DynamicClass");
                Object instance = clazz.getDeclaredConstructor().newInstance(); // Instantiate the DynamicClass
                compiledInstances.put(render, instance);
            }
            this.dynamicInstance = compiledInstances.get(render);
            this.dynamicMethod = dynamicInstance.getClass().getMethod("dynamicMethod", Graphics.class, int.class, int.class, int.class, int.class, int.class, int.class, Ether[][].class, double.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String generateClassCode(String render) {
        return  "import java.awt.Graphics;\n" +
                "import net.andrewcpu.solids.Ether;\n" +
                "import java.awt.*;\n" +
                "import net.andrewcpu.gui.renderers.DefaultRenderers;\n" +
                "import java.util.Random;\n" +
                "public class DynamicClass {\n" +
                "    public void dynamicMethod(Graphics g, int i, int j, int x, int y, int w, int h, Ether[][] pond, double value) {\n" +
                "        " + render + "\n" +
                "    }\n" +
                "}";
    }

    @Override
    public void render(Graphics g, int i, int j, int x, int y, int w, int h, Ether[][] pond, double value) {
        try {
            dynamicMethod.invoke(dynamicInstance, g, i, j, x, y, w, h, pond, value); // Use instance to call method
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

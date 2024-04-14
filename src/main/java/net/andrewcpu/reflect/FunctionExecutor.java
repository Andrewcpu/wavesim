package net.andrewcpu.reflect;

import org.codehaus.janino.SimpleCompiler;

public class FunctionExecutor {

    public static void executeFunction(String imports, String javaCode, Object... args) throws Exception {
        // Create a new compiler instance
        SimpleCompiler compiler = new SimpleCompiler();
        
        // Define the structure of the class containing the method with imports
        String classCode = 
            "import java.util.Random;\n" +
            "import java.awt.Graphics;\n" +
            "import java.awt.image.BufferedImage;\n" +
            imports + "\n" +
            "public class DynamicClass {\n" +
            "    public static void dynamicMethod(" + parameterTypesToString(args) + ") {\n" +
            "        " + javaCode + "\n" +
            "    }\n" +
            "}";
        
        // Compile the class
        compiler.cook(classCode);
        Class<?> clazz = compiler.getClassLoader().loadClass("DynamicClass");

        // Get the method to be invoked
        java.lang.reflect.Method method = clazz.getMethod("dynamicMethod", parameterClasses(args));

        // Invoke the method
        method.invoke(null, args);
    }

    private static String parameterTypesToString(Object[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(args[i].getClass().getName() + " arg" + i);
        }
        return sb.toString();
    }

    private static Class<?>[] parameterClasses(Object[] args) {
        Class<?>[] classes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            classes[i] = args[i].getClass();
        }
        return classes;
    }
}

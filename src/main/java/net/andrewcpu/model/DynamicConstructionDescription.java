package net.andrewcpu.model;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DynamicConstructionDescription<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Class<? extends T> solidBehavior;
    private Map<String, Object> constructorParameters;

    public DynamicConstructionDescription(Class<? extends T> solidBehavior, Map<String, Object> constructorParameters) {
        this.solidBehavior = solidBehavior;
        this.constructorParameters = constructorParameters;
    }

    public Class<? extends T> getSolidBehavior() {
        return solidBehavior;
    }

    public void setSolidBehavior(Class<? extends T> solidBehavior) {
        this.solidBehavior = solidBehavior;
    }

    public Map<String, Object> getConstructorParameters() {
        return constructorParameters;
    }

    public void setConstructorParameters(Map<String, Object> constructorParameters) {
        this.constructorParameters = constructorParameters;
    }

    public T build() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<? extends T> constructor = findMatchingConstructor();
        if (constructor != null) {
            Object[] parameters = getConstructorArguments(constructor);
            return constructor.newInstance(parameters);
        }
        throw new NoSuchMethodException("No matching constructor found for the provided parameters.");
    }

    private Constructor<? extends T> findMatchingConstructor() {
        Constructor<?>[] constructors = solidBehavior.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (isMatchingConstructor(constructor)) {
                return (Constructor<? extends T>) constructor;
            }
        }
        return null;
    }

    private boolean isMatchingConstructor(Constructor<?> constructor) {
        Parameter[] parameters = constructor.getParameters();
        if (parameters.length != constructorParameters.size()) {
            return false;
        }
        for (Parameter parameter : parameters) {
            if (!constructorParameters.containsKey(parameter.getName())) {
                return false;
            }
        }
        return true;
    }

    private Object[] getConstructorArguments(Constructor<? extends T> constructor) {
        Parameter[] parameters = constructor.getParameters();
        List<Object> arguments = new ArrayList<>();
        for (Parameter parameter : parameters) {
            String parameterName = parameter.getName();
            Object parameterValue = constructorParameters.get(parameterName);
            arguments.add(parameterValue);
        }
        return arguments.toArray();
    }
}
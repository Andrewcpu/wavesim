package net.andrewcpu.gui.state;

import net.andrewcpu.model.DynamicConstructionDescription;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public class BehaviorState implements Serializable {
    private String name;
    private Class<?> behaviorClass;
    private DynamicConstructionDescription<?> parameters;
    private UUID uuid;

    private String renderCode;

    public BehaviorState(String name, Class<?> behaviorClass, DynamicConstructionDescription<?> parameters, UUID uuid, String renderCode) {
        this.name = name;
        this.behaviorClass = behaviorClass;
        this.parameters = parameters;
        this.uuid = uuid == null ? UUID.randomUUID() : uuid; // Generate new UUID if null
        this.renderCode = renderCode;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    // getters and setters
    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Class<?> getBehaviorClass() {
        return behaviorClass;
    }

    public DynamicConstructionDescription<?> getParameters() {
        return parameters;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBehaviorClass(Class<?> behaviorClass) {
        this.behaviorClass = behaviorClass;
    }

    public void setParameters(DynamicConstructionDescription<?> parameters) {
        this.parameters = parameters;
    }

    public String getRenderCode() {
        return renderCode;
    }

    public void setRenderCode(String renderCode) {
        this.renderCode = renderCode;
    }
}

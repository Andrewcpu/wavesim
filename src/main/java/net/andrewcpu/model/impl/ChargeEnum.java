package net.andrewcpu.model.impl;

import net.andrewcpu.model.KeyPairEnum;

public enum ChargeEnum implements KeyPairEnum<String,Double> {
    POSITIVE("Positive", 1.0),
    NEGATIVE("Negative", -1.0);

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Double getValue() {
        return value;
    }

    private final String key;
    private final Double value;

    ChargeEnum(String key, Double value) {
        this.key = key;
        this.value = value;
    }
}

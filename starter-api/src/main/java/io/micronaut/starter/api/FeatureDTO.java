package io.micronaut.starter.api;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class FeatureDTO {
    private final String name;
    private final String description;

    public FeatureDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

package io.micronaut.starter.api;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.starter.feature.Feature;

/**
 * Represents an application feature.
 *
 * @author graemerocher
 * @since 1.0.0
 */
@Introspected
public class FeatureDTO {
    private final Feature feature;

    /**
     * Default constructor.
     * @param feature The feature
     */
    public FeatureDTO(Feature feature) {
        this.feature = feature;
    }

    /**
     * @return The name of the feature
     */
    public String getName() {
        return feature.getName();
    }

    /**
     * @return The description of the feature
     */
    public String getDescription() {
        return feature.getDescription();
    }
}

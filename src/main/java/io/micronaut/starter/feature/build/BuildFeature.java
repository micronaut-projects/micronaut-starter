package io.micronaut.starter.feature.build;

import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeaturePhase;

public interface BuildFeature extends Feature {

    @Override
    default boolean isVisible() {
        return false;
    }

    @Override
    default int getOrder() {
        return FeaturePhase.BUILD.getOrder();
    }
}

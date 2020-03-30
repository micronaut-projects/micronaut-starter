package io.micronaut.starter.feature.logging;

import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.feature.OneOfFeature;

public interface LoggingFeature extends OneOfFeature {

    @Override
    default Class<?> getFeatureClass() {
        return LoggingFeature.class;
    }
}

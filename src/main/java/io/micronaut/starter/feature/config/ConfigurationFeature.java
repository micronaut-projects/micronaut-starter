package io.micronaut.starter.feature.config;

import io.micronaut.starter.feature.OneOfFeature;

public interface ConfigurationFeature extends OneOfFeature {

    @Override
    default Class<?> getFeatureClass() {
        return ConfigurationFeature.class;
    }
}

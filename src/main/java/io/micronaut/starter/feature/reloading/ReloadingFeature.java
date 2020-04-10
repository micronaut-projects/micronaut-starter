package io.micronaut.starter.feature.reloading;

import io.micronaut.starter.feature.OneOfFeature;

public interface ReloadingFeature extends OneOfFeature {

    @Override
    default Class<?> getFeatureClass() {
        return ReloadingFeature.class;
    }
}

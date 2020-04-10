package io.micronaut.starter.feature.database;

import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.OneOfFeature;

public interface DataFeature extends OneOfFeature {

    @Override
    default Class<?> getFeatureClass() {
        return DataFeature.class;
    }
}

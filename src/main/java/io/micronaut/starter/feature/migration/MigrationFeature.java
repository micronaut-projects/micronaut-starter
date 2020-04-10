package io.micronaut.starter.feature.migration;

import io.micronaut.starter.feature.OneOfFeature;

public interface MigrationFeature extends OneOfFeature {

    @Override
    default Class<?> getFeatureClass() {
        return MigrationFeature.class;
    }
}

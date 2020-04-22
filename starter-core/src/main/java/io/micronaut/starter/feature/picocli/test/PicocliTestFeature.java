package io.micronaut.starter.feature.picocli.test;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.test.TestFeature;
import io.micronaut.starter.options.Options;

import java.util.List;

public interface PicocliTestFeature extends TestFeature {

    @Override
    default boolean shouldApply(ApplicationType applicationType,
                                Options options,
                                List<Feature> selectedFeatures) {
        return applicationType == ApplicationType.CLI && (options.getTestFramework() == getTestFramework() ||
                (options.getTestFramework() == null && options.getLanguage() == getDefaultLanguage()));
    }
}

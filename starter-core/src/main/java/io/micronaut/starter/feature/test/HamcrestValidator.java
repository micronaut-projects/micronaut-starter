package io.micronaut.starter.feature.test;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.options.Options;

import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class HamcrestValidator  implements FeatureValidator {
    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        if (features.stream().anyMatch(f -> f instanceof Hamcrest)) {
            if (features.stream().noneMatch(f -> (f instanceof Junit))) {
                throw new IllegalArgumentException("Hamcrest requires JUnit.");
            }
        }
    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {

    }
}
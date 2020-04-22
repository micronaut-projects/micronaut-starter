package io.micronaut.starter.feature.picocli.test;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.feature.test.TestFeature;

import java.util.function.Predicate;

public interface PicocliTestFeature extends TestFeature {

    @Override
    default int getOrder() {
        return FeaturePhase.TEST.getOrder() + 10;
    }

    @Override
    default Predicate<ApplicationType> appliesToByDefault() {
        return (applicationType) -> applicationType == ApplicationType.CLI;
    }
}

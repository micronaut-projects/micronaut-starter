package io.micronaut.starter.feature.crac;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class CracValidator implements FeatureValidator {

    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        if (options.getJavaVersion().majorVersion() < JdkVersion.JDK_17.majorVersion()) {
            throw new IllegalArgumentException("CRaC needs at least JDK 17");
        }
        if (features.stream().anyMatch(Crac.class::isInstance)
            && features.stream().anyMatch(GraalVM.class::isInstance)) {
            throw new IllegalArgumentException("CRaC and GraalVM cannot be combined");
        }
    }
}

package io.micronaut.starter.feature.graalvm;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;

import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class GraalFeatureValidator implements FeatureValidator {

    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {

    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        if (features.stream().anyMatch(f -> f instanceof GraalNativeImage)) {
            if (options.getLanguage() == Language.GROOVY) {
                throw new IllegalArgumentException("GraalVM is not supported in Groovy applications");
            }

            if (options.getJavaVersion() != JdkVersion.JDK_8 && options.getJavaVersion() != JdkVersion.JDK_11) {
                throw new IllegalArgumentException("GraalVM only supports JDK 8 and 11");
            }
        }
    }
}

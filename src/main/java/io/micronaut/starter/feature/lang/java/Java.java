package io.micronaut.starter.feature.lang.java;

import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.JavaApplicationFeature;
import io.micronaut.starter.feature.lang.LanguageFeature;
import io.micronaut.starter.feature.test.Junit;
import io.micronaut.starter.feature.test.TestFeature;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.util.VersionInfo;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class Java implements LanguageFeature {

    private final String version;
    private final Junit junit;
    private final List<JavaApplicationFeature> applicationFeatures;

    public Java(List<JavaApplicationFeature> applicationFeatures,
                Junit junit) {
        this.applicationFeatures = applicationFeatures;
        this.junit = junit;
        this.version = VersionInfo.getJdkVersion();
    }

    @Override
    public String getName() {
        return "java";
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public TestFeature getDefaultTestFeature() {
        return junit;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.hasApplicationFeature()) {
            applicationFeatures.stream()
                    .filter(f -> f.supports(featureContext.getCommand()))
                    .findFirst()
                    .ifPresent(featureContext::addFeature);
        }
    }

    @Override
    public boolean isJava() {
        return true;
    }
}

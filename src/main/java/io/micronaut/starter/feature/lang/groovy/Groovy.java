package io.micronaut.starter.feature.lang.groovy;

import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.GroovyApplicationFeature;
import io.micronaut.starter.feature.lang.LanguageFeature;
import io.micronaut.starter.feature.test.Spock;
import io.micronaut.starter.feature.test.TestFeature;
import io.micronaut.starter.options.TestFramework;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class Groovy implements LanguageFeature {

    private final Spock spock;
    private final List<GroovyApplicationFeature> applicationFeatures;

    public Groovy(List<GroovyApplicationFeature> applicationFeatures, Spock spock) {
        this.applicationFeatures = applicationFeatures;
        this.spock = spock;
    }

    @Override
    public String getName() {
        return "groovy";
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
    public String getVersion() {
        return null;
    }

    @Override
    public TestFeature getDefaultTestFeature() {
        return spock;
    }

    @Override
    public boolean isGroovy() {
        return true;
    }
}

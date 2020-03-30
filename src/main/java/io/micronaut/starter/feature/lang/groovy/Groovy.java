package io.micronaut.starter.feature.lang.groovy;

import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.lang.LanguageFeature;
import io.micronaut.starter.feature.test.Spock;
import io.micronaut.starter.options.TestFramework;

public class Groovy implements LanguageFeature {

    private final GroovyApplication groovyApplication;
    private final Spock spock;

    public Groovy(GroovyApplication groovyApplication, Spock spock) {
        this.groovyApplication = groovyApplication;
        this.spock = spock;
    }

    @Override
    public String getName() {
        return "groovy";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getTestFramework() == null) {
            featureContext.setTestFramework(TestFramework.spock, spock);
        }
        if (!featureContext.hasApplicationFeature()) {
            featureContext.addFeature(groovyApplication);
        }
    }

    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public boolean isGroovy() {
        return true;
    }
}

package io.micronaut.starter.feature.lang.groovy;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.LanguageFeature;
import io.micronaut.starter.options.TestFramework;

public class Groovy implements LanguageFeature {

    @Override
    public String getName() {
        return "groovy";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getTestFramework() == null) {
            featureContext.setTestFramework(TestFramework.spock);
        }
        if (!featureContext.hasApplicationFeature()) {
            featureContext.addFeature(new GroovyApplication());
        }
    }

    @Override
    public void apply(CommandContext commandContext) {

    }

    @Override
    public String getVersion() {
        return null;
    }
}

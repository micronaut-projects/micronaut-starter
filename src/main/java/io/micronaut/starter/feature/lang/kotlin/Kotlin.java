package io.micronaut.starter.feature.lang.kotlin;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.LanguageFeature;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

public class Kotlin implements LanguageFeature {

    @Override
    public String getName() {
        return "kotlin";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getTestFramework() == null) {
            featureContext.setTestFramework(TestFramework.kotlintest);
        }
        if (!featureContext.hasApplicationFeature()) {
            featureContext.addFeature(new KotlinApplication());
        }
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getProjectProperties().put("kotlinVersion", getVersion());
    }

    @Override
    public String getVersion() {
        return "1.3.50";
    }
}

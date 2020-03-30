package io.micronaut.starter.feature.lang.kotlin;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.lang.LanguageFeature;
import io.micronaut.starter.feature.test.KotlinTest;
import io.micronaut.starter.options.TestFramework;

import javax.inject.Singleton;

@Singleton
public class Kotlin implements LanguageFeature {

    private final KotlinApplication kotlinApplication;
    private final KotlinTest kotlinTest;

    public Kotlin(KotlinApplication kotlinApplication, KotlinTest kotlinTest) {
        this.kotlinApplication = kotlinApplication;
        this.kotlinTest = kotlinTest;
    }

    @Override
    public String getName() {
        return "kotlin";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getTestFramework() == null) {
            featureContext.setTestFramework(TestFramework.kotlintest, kotlinTest);
        }
        if (!featureContext.hasApplicationFeature()) {
            featureContext.addFeature(kotlinApplication);
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

    @Override
    public boolean isKotlin() {
        return true;
    }
}

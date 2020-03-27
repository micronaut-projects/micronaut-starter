package io.micronaut.starter.feature.lang.java;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.LanguageFeature;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.util.VersionInfo;

public class Java implements LanguageFeature {

    private final String version;

    public Java() {
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
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getTestFramework() == null) {
            featureContext.setTestFramework(TestFramework.junit);
        }
        if (!featureContext.hasApplicationFeature()) {
            featureContext.addFeature(new JavaApplication());
        }
    }

    @Override
    public void apply(CommandContext commandContext) {
    }
}

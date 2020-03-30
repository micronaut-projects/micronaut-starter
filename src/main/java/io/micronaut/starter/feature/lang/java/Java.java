package io.micronaut.starter.feature.lang.java;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.lang.LanguageFeature;
import io.micronaut.starter.feature.test.Junit;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.util.VersionInfo;

import javax.inject.Singleton;

@Singleton
public class Java implements LanguageFeature {

    private final String version;
    private final JavaApplication javaApplication;
    private final Junit junit;

    public Java(JavaApplication javaApplication, Junit junit) {
        this.javaApplication = javaApplication;
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
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (featureContext.getTestFramework() == null) {
            featureContext.setTestFramework(TestFramework.junit, junit);
        }
        if (!featureContext.hasApplicationFeature()) {
            featureContext.addFeature(javaApplication);
        }
    }

    @Override
    public boolean isJava() {
        return true;
    }
}

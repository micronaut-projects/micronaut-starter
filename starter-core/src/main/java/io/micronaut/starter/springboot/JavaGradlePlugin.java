package io.micronaut.starter.springboot;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.GradleSpecificFeature;
import jakarta.inject.Singleton;

@Singleton
public class JavaGradlePlugin implements GradleSpecificFeature {
    @Override
    public String getName() {
        return "java-gradle-plugin";
    }

    @Override
    public String getTitle() {
        return "Java Gradle Plugin";
    }

    @Override
    public String getDescription() {
        return "Adds the Java Gradle Plugin which adds Java compilation along with testing and bundling capabilities to a project.";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.gradle.org/current/userguide/java_plugin.html";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("java")
                    .build());
        }
    }
}

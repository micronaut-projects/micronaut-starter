package io.micronaut.starter.feature.build.gradle;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
class MicronautOpenrewriteGradlePluginFeature implements DefaultFeature {
    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return options.getBuildTool().isGradle();
    }

    @Override
    public String getName() {
        return "micronaut-openrewrite-gradle-plugin";
    }

    @Override
    public String getTitle() {
        return "Micronaut Openrewrite Gradle Plugin";
    }

    @Override
    public String getDescription() {
        return "Adds the Micronaut Openrewrite Gradle Plugin to the project";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addBuildPlugin(MicronautOpenrewriteGradlePlugin.builder().build());
    }
}

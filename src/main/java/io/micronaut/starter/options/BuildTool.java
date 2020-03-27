package io.micronaut.starter.options;

import io.micronaut.starter.feature.BuildFeature;
import io.micronaut.starter.feature.build.gradle.Gradle;
import io.micronaut.starter.feature.build.maven.Maven;

public enum BuildTool {

    gradle(new Gradle()), maven(new Maven());

    private final BuildFeature feature;

    BuildTool(BuildFeature feature) {
        this.feature = feature;
    }

    public BuildFeature getFeature() {
        return feature;
    }
}

package io.micronaut.starter.feature;

import io.micronaut.starter.Project;

public interface ApplicationFeature extends Feature {

    String mainClassName(Project project);

    @Override
    default boolean isVisible() {
        return false;
    }
}

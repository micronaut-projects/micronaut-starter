package io.micronaut.starter;

import io.micronaut.starter.feature.Feature;

public class AnnotationApi implements Feature {

    @Override
    public String getName() {
        return "annotation-api";
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}

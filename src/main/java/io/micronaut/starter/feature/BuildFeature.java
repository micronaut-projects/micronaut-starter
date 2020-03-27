package io.micronaut.starter.feature;

public interface BuildFeature extends Feature {

    @Override
    default boolean isVisible() {
        return false;
    }
}

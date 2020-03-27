package io.micronaut.starter.feature;

public interface LanguageFeature extends Feature {

    @Override
    default boolean isVisible() {
        return false;
    }

    String getVersion();
}

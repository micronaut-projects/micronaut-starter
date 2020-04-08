package io.micronaut.starter.feature.lang;

import io.micronaut.context.annotation.DefaultScope;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.feature.test.TestFeature;

public interface LanguageFeature extends Feature {

    @Override
    default boolean isVisible() {
        return false;
    }

    String getVersion();

    TestFeature getDefaultTestFeature();

    @Override
    default int getOrder() {
        return FeaturePhase.LANGUAGE.getOrder();
    }

    default boolean isJava() {
        return false;
    }

    default boolean isGroovy() {
        return false;
    }

    default boolean isKotlin() {
        return false;
    }
}

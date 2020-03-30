package io.micronaut.starter.feature;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

import java.util.List;
import java.util.Optional;

public interface Feature {

    String getName();

    default int getOrder() {
        return 0;
    }

    default void processSelectedFeatures(FeatureContext featureContext) {

    }

    default void apply(CommandContext commandContext) {

    }

    default boolean supports(String command) {
        return true;
    }

    default boolean isVisible() {
        return true;
    }

    default Optional<Language> getRequiredLanguage() {
        return Optional.empty();
    }

}

package io.micronaut.starter.feature;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.options.Language;

import java.util.Optional;

public interface Feature {

    String getName();

    default int getOrder() {
        return FeaturePhase.DEFAULT.getOrder();
    }

    default void processSelectedFeatures(FeatureContext featureContext) {

    }

    default void apply(CommandContext commandContext) {

    }

    default boolean supports(MicronautCommand command) {
        return true;
    }

    default boolean isVisible() {
        return true;
    }

    default Optional<Language> getRequiredLanguage() {
        return Optional.empty();
    }

}

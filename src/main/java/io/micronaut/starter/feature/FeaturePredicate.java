package io.micronaut.starter.feature;

import java.util.Optional;
import java.util.function.Predicate;

public interface FeaturePredicate extends Predicate<Feature> {

    default Optional<String> getWarning() {
        return Optional.empty();
    }
}

package io.micronaut.starter.options;

import io.micronaut.starter.feature.Feature;

import java.util.*;

public enum Language {
    java,
    groovy,
    kotlin;

    public static Language infer(List<Feature> features) {
        return features.stream()
                .map(Feature::getRequiredLanguage)
                .filter(Optional::isPresent)
                .findFirst()
                .map(Optional::get)
                .orElse(null);
    }
}

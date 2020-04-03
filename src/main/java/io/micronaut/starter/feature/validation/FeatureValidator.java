package io.micronaut.starter.feature.validation;

import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Language;

import java.util.List;

public interface FeatureValidator {

    void validate(Language language, List<Feature> features);
}

package io.micronaut.starter.feature.validation;

import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.OneOfFeature;
import io.micronaut.starter.options.Language;

import javax.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class OneOfFeatureValidator implements FeatureValidator {

    @Override
    public void validate(Language language, List<Feature> features) {
        Set<Class<?>> oneOfFeatures = features.stream()
                .filter(feature -> feature instanceof OneOfFeature)
                .map(OneOfFeature.class::cast)
                .map(OneOfFeature::getFeatureClass)
                .collect(Collectors.toSet());

        for (Class<?> featureClass: oneOfFeatures) {
            List<String> matches = features.stream()
                    .filter(feature -> featureClass.isAssignableFrom(feature.getClass()))
                    .map(Feature::getName)
                    .collect(Collectors.toList());
            if (matches.size() > 1) {
                throw new IllegalArgumentException(String.format("There can only be one of the following features selected: %s", matches));
            }
        }
    }
}

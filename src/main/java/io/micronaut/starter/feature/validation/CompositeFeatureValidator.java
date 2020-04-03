package io.micronaut.starter.feature.validation;

import io.micronaut.context.annotation.Primary;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Language;

import javax.inject.Singleton;
import java.util.List;

@Primary
@Singleton
public class CompositeFeatureValidator implements FeatureValidator {

    private final List<FeatureValidator> featureValidators;

    public CompositeFeatureValidator(List<FeatureValidator> featureValidators) {
        this.featureValidators = featureValidators;
    }

    public void validate(Language language, List<Feature> features) {
        for (FeatureValidator featureValidator: featureValidators) {
            featureValidator.validate(language, features);
        }
    }
}

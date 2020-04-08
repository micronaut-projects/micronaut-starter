package io.micronaut.starter.feature;

import io.micronaut.starter.command.MicronautCommand;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class FeatureContextFactory {

    private final FeatureValidator featureValidator;

    public FeatureContextFactory(FeatureValidator featureValidator) {
        this.featureValidator = featureValidator;
    }

    public FeatureContext build(AvailableFeatures availableFeatures,
                                List<String> selectedFeatures,
                                MicronautCommand command,
                                Language language,
                                BuildTool buildTool,
                                TestFramework testFramework) {
        final List<Feature> features = new ArrayList<>(8);
        for (String name: selectedFeatures) {
            Feature feature = availableFeatures.findFeature(name).orElse(null);
            if (feature != null) {
                features.add(feature);
            } else {
                throw new IllegalArgumentException("The requested feature does not exist: " + name);
            }
        }

        Language lang = determineLanguage(language, features);

        availableFeatures.getAllFeatures()
                .filter(f -> f instanceof DefaultFeature)
                .filter(f -> ((DefaultFeature) f).shouldApply(command, lang, testFramework, buildTool, features))
                .forEach(features::add);

        featureValidator.validate(lang, features);

        return new FeatureContext(lang, testFramework, buildTool, command, features);
    }

    Language determineLanguage(Language language, List<Feature> features) {
        if (language == null) {
            language = Language.infer(features);
        }
        if (language == null) {
            language = Language.java;
        }
        return language;
    }
}

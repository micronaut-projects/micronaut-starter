/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.AvailableFeatures;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.options.Language;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ContextFactory {

    private final FeatureValidator featureValidator;

    public ContextFactory(FeatureValidator featureValidator) {
        this.featureValidator = featureValidator;
    }

    public FeatureContext createFeatureContext(AvailableFeatures availableFeatures,
                                               List<String> selectedFeatures,
                                               ApplicationType command,
                                               Options options) {
        final List<Feature> features = new ArrayList<>(8);
        for (String name: selectedFeatures) {
            Feature feature = availableFeatures.findFeature(name).orElse(null);
            if (feature != null) {
                features.add(feature);
            } else {
                throw new IllegalArgumentException("The requested feature does not exist: " + name);
            }
        }

        Language language = determineLanguage(options.getLanguage(), features);
        Options newOptions = options.withLanguage(language);

        availableFeatures.getAllFeatures()
                .filter(f -> f instanceof DefaultFeature)
                .filter(f -> ((DefaultFeature) f).shouldApply(command, newOptions, features))
                .forEach(features::add);

        featureValidator.validate(newOptions, features);

        return new FeatureContext(newOptions, command, features);
    }

    public GeneratorContext createGeneratorContext(Project project,
                                                   FeatureContext featureContext,
                                                   ConsoleOutput consoleOutput) {
        featureContext.processSelectedFeatures();

        List<Feature> featureList = featureContext.getFinalFeatures(consoleOutput);

        featureValidator.validate(featureContext.getOptions(), featureList);

        return new GeneratorContext(project, featureContext.getCommand(), featureContext.getOptions(), featureList);
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

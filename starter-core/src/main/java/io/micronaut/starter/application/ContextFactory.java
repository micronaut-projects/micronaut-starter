/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.application;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.order.OrderUtil;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.DefaultCoordinateResolver;
import io.micronaut.starter.feature.AvailableFeatures;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.io.ConsoleOutput;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;

import jakarta.inject.Singleton;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

@Singleton
public class ContextFactory {

    private final FeatureValidator featureValidator;
    private final DefaultCoordinateResolver coordinateResolver;

    public ContextFactory(FeatureValidator featureValidator,
                          DefaultCoordinateResolver coordinateResolver) {
        this.featureValidator = featureValidator;
        this.coordinateResolver = coordinateResolver;
    }

    public FeatureContext createFeatureContext(AvailableFeatures availableFeatures,
                                               List<String> selectedFeatures,
                                               ApplicationType applicationType,
                                               Options options,
                                               @Nullable OperatingSystem operatingSystem) {
        final Set<Feature> features = Collections.newSetFromMap(new IdentityHashMap<>(8));
        for (String name: selectedFeatures) {
            Feature feature = availableFeatures.findFeature(name, true).orElse(null);
            if (feature != null) {
                features.add(feature);
            } else {
                throw new IllegalArgumentException("The requested feature does not exist: " + name);
            }
        }

        Language language = determineLanguage(options.getLanguage(), features);
        Options newOptions = options.withLanguage(language)
                .withBuildTool(determineBuildTool(language, options.getBuildTool()));

        availableFeatures.getAllFeatures()
                .filter(DefaultFeature.class::isInstance)
                .sorted(OrderUtil.COMPARATOR.reversed())
                .filter(f -> ((DefaultFeature) f).shouldApply(applicationType, newOptions, features))
                .forEach(features::add);

        featureValidator.validatePreProcessing(newOptions, applicationType, features);

        return new FeatureContext(newOptions, applicationType, operatingSystem, features);
    }

    public GeneratorContext createGeneratorContext(Project project,
                                                   FeatureContext featureContext,
                                                   ConsoleOutput consoleOutput) {
        featureContext.processSelectedFeatures();

        Set<Feature> featureList = featureContext.getFinalFeatures(consoleOutput);

        featureValidator.validatePostProcessing(featureContext.getOptions(), featureContext.getApplicationType(), featureList);

        return new GeneratorContext(project, featureContext.getApplicationType(), featureContext.getOptions(), featureContext.getOperatingSystem(), featureList, coordinateResolver);
    }

    Language determineLanguage(Language language, Set<Feature> features) {
        if (language == null) {
            language = Language.infer(features);
        }
        if (language == null) {
            language = Language.DEFAULT_OPTION;
        }
        return language;
    }

    BuildTool determineBuildTool(Language language, BuildTool buildTool) {
        if (buildTool == null) {
            buildTool = language.getDefaults().getBuild();
        }
        return buildTool;
    }
}

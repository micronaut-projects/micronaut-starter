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
package io.micronaut.starter.feature.server;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.options.Options;

import jakarta.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validates that features dependent on a Micronaut server are not included when
 * a 3rd party (i.e. non-Micronaut) server is selected.
 */
@Singleton
public class MicronautServerDependentValidator implements FeatureValidator {
    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {

        features.stream().filter(f -> f instanceof ThirdPartyServerFeature).findFirst().ifPresent(feature -> {
            List<String> dependents = features.stream()
            .filter(f -> f instanceof MicronautServerDependent)
            .map(Feature::getName)
            .sorted()
            .collect(Collectors.toList());

            if (!dependents.isEmpty()) {
                throw new IllegalArgumentException(feature.getName() +
                    " cannot be used with these features that depend on a Micronaut Server: " +
                    dependents);
            }
        });
    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {

    }
}

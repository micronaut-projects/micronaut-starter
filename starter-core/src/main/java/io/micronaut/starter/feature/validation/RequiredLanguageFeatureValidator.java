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
package io.micronaut.starter.feature.validation;

import io.micronaut.core.util.ArrayUtils;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.LanguageSpecificFeature;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Language;

import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class RequiredLanguageFeatureValidator implements FeatureValidator {

    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        if (options != null) {
            Map<String, Language[]> incomaptibleFeatures = features.stream()
                    .filter(LanguageSpecificFeature.class::isInstance)
                    .map(LanguageSpecificFeature.class::cast)
                    .filter(f -> ArrayUtils.isNotEmpty(f.getRequiredLanguages()))
                    .filter(f -> !Arrays.asList(f.getRequiredLanguages()).contains(options.getLanguage()))
                    .collect(Collectors.toMap(Feature::getName, LanguageSpecificFeature::getRequiredLanguages));

            if (!incomaptibleFeatures.isEmpty()) {
                throw new IllegalArgumentException(String.format("The selected features are incompatible with language %s: %s.", options.getLanguage(),
                        incomaptibleFeatures
                                .entrySet()
                                .stream()
                                .map(e -> String.format("%s requires %s", e.getKey(), Arrays.toString(e.getValue())))
                                .collect(Collectors.joining(", "))));
            }
        }
    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {

    }
}

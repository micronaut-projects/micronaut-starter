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

import io.micronaut.starter.Options;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.OneOfFeature;

import javax.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class OneOfFeatureValidator implements FeatureValidator {

    @Override
    public void validate(Options options, List<Feature> features) {
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

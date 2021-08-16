/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.feature.reactive;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.options.Options;
import javax.inject.Singleton;
import java.util.Set;

/**
 * Validator which throws an exception if the user selects more than one {@link ReactiveFeature}.
 * @author Sergio del Amo
 * @since 2.5.13
 */
@Singleton
public class ReactiveFeatureValidator implements FeatureValidator  {
    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        if (moreThanOneReactiveFeature(features)) {
            throw new IllegalArgumentException("You cannot select more than one reactive library");
        }
    }

    private static boolean moreThanOneReactiveFeature(Set<Feature> features) {
        return features.stream()
                .filter(f -> f instanceof ReactiveFeature)
                .count() > 1;
    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {

    }
}

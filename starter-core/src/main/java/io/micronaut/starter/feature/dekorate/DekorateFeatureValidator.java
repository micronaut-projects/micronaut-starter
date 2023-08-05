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
package io.micronaut.starter.feature.dekorate;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;

import jakarta.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Dekorate feature validator.
 *
 * @author Pavol Gressa
 * @since 2.1
 */
@Singleton
public class DekorateFeatureValidator implements FeatureValidator {

    private final List<AbstractDekoratePlatformFeature> dekoratePlatformFeatures;

    public DekorateFeatureValidator(List<AbstractDekoratePlatformFeature> dekoratePlatformFeatures) {
        this.dekoratePlatformFeatures = dekoratePlatformFeatures;
    }

    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        if (features.stream().anyMatch(AbstractDekorateFeature.class::isInstance)) {
            if (options.getLanguage() == Language.GROOVY) {
                throw new IllegalArgumentException("Dekorate is not supported in Groovy applications");
            }

            if (features.stream().noneMatch(AbstractDekoratePlatformFeature.class::isInstance)) {
                throw new IllegalArgumentException(String.format("At least one of %s features must be selected in order to use Dekorate properly",
                        dekoratePlatformFeatures.stream().map(Feature::getName).collect(Collectors.toList())));
            }
        }
    }
}

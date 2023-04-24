/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.starter.feature.build;

import java.util.Set;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.dekorate.AbstractDekorateFeature;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

@Singleton
public class KotlinSymbolProcessingValidator implements FeatureValidator {
    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {

    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        if (features.stream().anyMatch(KotlinSymbolProcessing.class::isInstance)) {
            if (options.getLanguage() != Language.KOTLIN) {
                throw new IllegalArgumentException("Kotlin Symbol Processing (KSP) only supports Kotlin");
            }
            if (features.stream().anyMatch(KotlinSymbolProcessing.class::isInstance) && !options.getBuildTool().isGradle()) {
                throw new IllegalArgumentException("Kotlin Symbol Processing (KSP) is only supported by Gradle");
            }
        }
    }
}

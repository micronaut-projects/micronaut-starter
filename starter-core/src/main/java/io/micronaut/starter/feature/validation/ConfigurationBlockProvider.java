/*
 * Copyright 2017-2026 original authors
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
package io.micronaut.starter.feature.validation;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Set;

@Singleton
public class ConfigurationBlockProvider {

    @Nullable
    public ConfigurationValidationBlock configurationValidation(@NonNull GeneratorContext generatorContext) {
        Set<Feature> features = generatorContext.getFeatures().getFeatures();
        ConfigurationValidationBlock configurationValidation = null;
        if (features.stream().anyMatch(ConfigurationValidationFeature.class::isInstance)) {
            ConfigurationValidationBlock.Builder configurationValidationBuilder = ConfigurationValidationBlock.builder()
                    .enabled(true)
                    .failOnNotPresent(true)
                    .cacheEnabled(true);
            if (features.stream().anyMatch(DependencyInjectionValidation.class::isInstance)) {
                configurationValidationBuilder.validateDependencyInjection(true);
            }
            configurationValidation = configurationValidationBuilder.build();
        }
        return configurationValidation;
    }
}

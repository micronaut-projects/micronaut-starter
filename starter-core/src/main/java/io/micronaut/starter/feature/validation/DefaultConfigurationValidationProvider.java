/*
 * Copyright 2017-2025 original authors
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
import jakarta.inject.Singleton;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Singleton
public class DefaultConfigurationValidationProvider implements ConfigurationValidationProvider {
    public static final boolean DEFAULT_VALIDATE_DEPENDENCY_INJECTION = false;
    public static final boolean DEFAULT_FAIL_ON_NOT_PRESENT = true;
    public static final boolean DEFAULT_CACHE = true;
    public static final boolean DEFAULT_ENABLED = true;
    private final boolean enabled;
    private final boolean cache;
    private final boolean failOnNotPresent;
    private final boolean validateDependencyInjection;
    private ConfigurationValidationBlock configurationValidation;

    public DefaultConfigurationValidationProvider() {
        this(DEFAULT_ENABLED,
                DEFAULT_CACHE,
                DEFAULT_FAIL_ON_NOT_PRESENT,
                DEFAULT_VALIDATE_DEPENDENCY_INJECTION);
    }

    public DefaultConfigurationValidationProvider(boolean enabled,
                                                  boolean cache,
                                                  boolean failOnNotPresent,
                                                  boolean validateDependencyInjection) {
        this.enabled = enabled;
        this.cache = cache;
        this.failOnNotPresent = failOnNotPresent;
        this.validateDependencyInjection = validateDependencyInjection;
        this.configurationValidation = configurationValidation();
    }

    @Override
    public @Nullable ConfigurationValidationBlock configurationValidation(@NonNull GeneratorContext generatorContext) {
        //return generatorContext.getBuildTool().isGradle() ? configurationValidation : null;
        return configurationValidation;
    }

    protected ConfigurationValidationBlock configurationValidation() {
        return builder().build();
    }

    protected ConfigurationValidationBlock.Builder builder() {
        return ConfigurationValidationBlock.builder()
                .validateDependencyInjection(validateDependencyInjection)
                .failOnNotPresent(failOnNotPresent)
                .cacheEnabled(cache)
                .enabled(enabled);
    }
}

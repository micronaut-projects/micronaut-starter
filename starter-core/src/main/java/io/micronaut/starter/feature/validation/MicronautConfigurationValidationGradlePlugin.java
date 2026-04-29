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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;
import org.jspecify.annotations.NonNull;
import java.util.Set;

@Singleton
class MicronautConfigurationValidationGradlePlugin implements DefaultFeature {
    public static final String MICRONAUT_GRADLE_PLUGIN_CONFIGURATION_VALIDATION_ID = "io.micronaut.configuration.validation";
    public static final String ARTIFACT_ID = "micronaut-configuration-validation-plugin";
    private static final int GRADLE_PLUGIN_ORDER = 11;

    private final ConfigurationValidationProvider configurationValidationProvider;

    MicronautConfigurationValidationGradlePlugin(ConfigurationValidationProvider configurationValidationProvider) {
        this.configurationValidationProvider = configurationValidationProvider;
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return true;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle() && configurationValidationProvider.configurationValidation(generatorContext) != null) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id(MICRONAUT_GRADLE_PLUGIN_CONFIGURATION_VALIDATION_ID)
                    .lookupArtifactId(ARTIFACT_ID)
                    .order(GRADLE_PLUGIN_ORDER)
                    .build());
        }
    }

    @Override
    public String getCategory() {
        return Category.VALIDATION;
    }

    @Override
    @NonNull
    public String getName() {
        return "micronaut-configuration-validation-gradle-plugin";
    }

    @Override
    public String getTitle() {
        return "Micronaut Configuration Validation Gradle Plugin";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}

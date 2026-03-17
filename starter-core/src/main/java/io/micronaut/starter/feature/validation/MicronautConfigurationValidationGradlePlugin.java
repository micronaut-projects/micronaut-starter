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
import io.micronaut.starter.build.gradle.GradleDsl;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Options;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;
import io.micronaut.starter.rocker.feature.validation.micronautConfigurationValidationGradlePlugin;
import java.util.Set;


@Singleton
class MicronautConfigurationValidationGradlePlugin implements DefaultFeature {
    public static final boolean DEFAULT_VALIDATE_DEPENDENCY_INJECTION = true;
    public static final boolean DEFAULT_FAIL_ON_NOT_PRESENT = true;
    public static final String MICRONAUT_GRADLE_PLUGIN_TEST_RESOURCES_ID = "io.micronaut.configuration.validation";
    public static final String ARTIFACT_ID = "micronaut-configuration-validation-plugin";

    @Override
    public String getName() {
        return ARTIFACT_ID;
    }

    @Override
    public String getTitle() {
        return "Micronaut Configuration Validation Plugin";
    }

    @Override
    public String getDescription() {
        return "The Micronaut configuration validation plugin validates your configuration files against JSON Schemas published by Micronaut modules and generated from @ConfigurationProperties.";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            boolean validateDependencyInjection = DEFAULT_VALIDATE_DEPENDENCY_INJECTION;
            boolean failOnNotPresent = DEFAULT_FAIL_ON_NOT_PRESENT;
            validateDependencyInjection = true;
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id(MICRONAUT_GRADLE_PLUGIN_TEST_RESOURCES_ID)
                    .lookupArtifactId(ARTIFACT_ID)
                    .extension(new RockerWritable(micronautConfigurationValidationGradlePlugin.template(generatorContext.getBuildTool().getGradleDsl().orElse(GradleDsl.KOTLIN), failOnNotPresent, validateDependencyInjection)))
                    .build());
        }
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-gradle-plugin/latest/#configuration-validation";
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return options.getBuildTool().isGradle();
    }

    @Override
    public String getCategory() {
        return Category.VALIDATION;
    }
}

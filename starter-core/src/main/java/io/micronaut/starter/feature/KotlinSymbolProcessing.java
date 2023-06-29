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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.build.gradle.KotlinSymbolProcessingFeature;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.GradleSpecificFeature;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class KotlinSymbolProcessing implements KotlinSupportFeature, DefaultFeature, GradleSpecificFeature, KotlinSymbolProcessingFeature {

    @Override
    @NonNull
    public String getName() {
        return "ksp";
    }

    @Override
    public boolean isPreview() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Kotlin Symbol Processing (KSP)";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for processing source code at compilation time with Kotlin Symbol Processing (KSP).";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://docs.micronaut.io/latest/guide/#kotlin";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://kotlinlang.org/docs/ksp-overview.html";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addBuildPlugins(generatorContext);
    }

    @Override
    public void addBuildPlugins(@NonNull GeneratorContext generatorContext) {
        KotlinSupportFeature.super.addBuildPlugins(generatorContext);
        if (KotlinSupportFeature.shouldApply(generatorContext)) {
            generatorContext.addBuildPlugin(GradlePlugin.of("com.google.devtools.ksp", "com.google.devtools.ksp.gradle.plugin"));
        }
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return options.getBuildTool().isGradle() &&
                KotlinSupportFeature.shouldApply(options.getLanguage(), options.getTestFramework()) && selectedFeatures.stream().noneMatch(KotlinSupportFeature.class::isInstance);
    }
}

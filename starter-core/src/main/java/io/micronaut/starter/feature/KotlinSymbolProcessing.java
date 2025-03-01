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
package io.micronaut.starter.feature;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.buildtools.gradle.GradleSpecificFeature;
import io.micronaut.projectgen.core.feature.DefaultFeature;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.core.utils.OptionUtils;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.gradle.GradlePlugin;
import io.micronaut.projectgen.core.feature.KotlinSymbolProcessingFeature;
import io.micronaut.starter.feature.build.KotlinSupportFeature;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Requires(property = "micronaut.starter.feature.ksp.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
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
    public String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://docs.micronaut.io/latest/guide/#kotlin";
    }

    @Override
    public String getThirdPartyDocumentation(GeneratorContext generatorContext) {
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
    public boolean shouldApply(Options options, Set<Feature> selectedFeatures) {
        return OptionUtils.hasGradleBuildTool(options)
                && KotlinSupportFeature.shouldApply(options.language(), options.testFramework())
                && selectedFeatures.stream().noneMatch(KotlinSupportFeature.class::isInstance);
    }
}

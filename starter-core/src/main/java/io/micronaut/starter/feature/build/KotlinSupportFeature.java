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
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.OneOfFeature;
import io.micronaut.starter.feature.database.JpaFeature;
import io.micronaut.starter.feature.lang.LanguageFeature;
import io.micronaut.starter.feature.test.TestFeature;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;

/**
 * Marker interface for a feature which adds support for the {@link Language#KOTLIN} programming language.
 * @since 4.0.0
 * @author Sergio del Amo
 */
public interface KotlinSupportFeature extends OneOfFeature {
    @Override
    default Class<?> getFeatureClass() {
        return KotlinSupportFeature.class;
    }

    @Override
    default boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    default String getCategory() {
        return Category.LANGUAGES;
    }

    default void addBuildPlugins(@NonNull GeneratorContext generatorContext) {
        if (shouldApply(generatorContext)) {
            generatorContext.addBuildPlugin(GradlePlugin.of("org.jetbrains.kotlin.jvm", "kotlin-gradle-plugin"));
            generatorContext.addBuildPlugin(GradlePlugin.of("org.jetbrains.kotlin.plugin.allopen", "kotlin-allopen"));
            if (generatorContext.getFeatures().isFeaturePresent(JpaFeature.class)) {
                generatorContext.addBuildPlugin(GradlePlugin.of("org.jetbrains.kotlin.plugin.jpa", "kotlin-noarg"));
            }
        }
    }

    static boolean shouldApply(GeneratorContext generatorContext) {
        return generatorContext.getBuildTool().isGradle() &&
                KotlinSupportFeature.shouldApply(generatorContext.getFeatures().language(), generatorContext.getFeatures().testFramework());
    }

    static boolean shouldApply(LanguageFeature languageFeature, TestFeature testFeature) {
        return languageFeature.isKotlin() || (testFeature != null && testFeature.isKotlinTestFramework());
    }

    static boolean shouldApply(Language language, TestFramework testFramework) {
        return language == Language.KOTLIN || (testFramework != null && testFramework.isKotlinTestFramework());
    }
}

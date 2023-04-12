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
package io.micronaut.starter.feature.build;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.JpaFeature;
import io.micronaut.starter.feature.lang.LanguageFeature;
import io.micronaut.starter.feature.test.TestFeature;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.TestFramework;
import jakarta.inject.Singleton;

@Singleton
public class KotlinBuildPlugins implements Feature {
    public static final int ORDER = -10;

    @Override
    public String getName() {
        return "kotlin-build";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {

        if (generatorContext.getBuildTool().isGradle() &&
                shouldApply(generatorContext.getFeatures().language(), generatorContext.getFeatures().testFramework())) {
            generatorContext.addBuildPlugin(gradlePlugin("org.jetbrains.kotlin.jvm", "kotlin-gradle-plugin"));
            if (generatorContext.isFeaturePresent(KotlinSymbolProcessing.class)) {
                generatorContext.addBuildPlugin(gradlePlugin("com.google.devtools.ksp", "com.google.devtools.ksp.gradle.plugin"));
            } else {
                generatorContext.addBuildPlugin(gradlePlugin("org.jetbrains.kotlin.kapt", "kotlin-gradle-plugin"));
            }
            generatorContext.addBuildPlugin(gradlePlugin("org.jetbrains.kotlin.plugin.allopen", "kotlin-allopen"));
            if (generatorContext.getFeatures().isFeaturePresent(JpaFeature.class)) {
                generatorContext.addBuildPlugin(gradlePlugin("org.jetbrains.kotlin.plugin.jpa", "kotlin-noarg"));
            }
        }
    }

    private static GradlePlugin gradlePlugin(String id, String lookupArtifactId) {
        return GradlePlugin.builder()
                .id(id)
                .lookupArtifactId(lookupArtifactId)
                .order(ORDER)
                .build();
    }

    public boolean shouldApply(FeatureContext featureContext) {
        return shouldApply(featureContext.getLanguage(), featureContext.getTestFramework());
    }

    public boolean shouldApply(LanguageFeature languageFeature, TestFeature testFeature) {
        return languageFeature.isKotlin() || testFeature.isKotlinTestFramework();
    }

    public boolean shouldApply(Language language, TestFramework testFramework) {
        return language == Language.KOTLIN || testFramework.isKotlinTestFramework();
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}

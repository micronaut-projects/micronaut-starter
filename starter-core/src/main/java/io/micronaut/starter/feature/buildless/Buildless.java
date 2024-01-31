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
package io.micronaut.starter.feature.buildless;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradleFile;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.CommunityFeature;
import io.micronaut.starter.feature.GradleSpecificFeature;
import io.micronaut.starter.feature.build.gradle.templates.buildlessGradlePlugin;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

@Singleton
public class Buildless implements CommunityFeature, GradleSpecificFeature {
    public static final String NAME = "buildless";
    public static final String BUILDLESS_PLUGIN_ARTIFACT = "buildless-plugin-gradle";
    private static final String FEATURE_NAME_BUILDLESS = "buildless";
    private static final String BUILDLESS_PLUGIN_ID = "build.less";

    @Override
    public String getName() {
        return FEATURE_NAME_BUILDLESS;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCommunityFeatureTitle() {
        return "Buildless";
    }

    @Override
    public String getCommunityFeatureName() {
        return Buildless.NAME;
    }

    @Override
    public String getCommunityContributor() {
        return "@sgammon";
    }

    @Override
    public String getTitle() {
        return "Buildless";
    }

    @Override
    public String getDescription() {
        return "Lightning-fast build caching CDN, compatible with Gradle, Maven, Bazel, and Gradle Enterprise.";
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.less.build/";
    }

    private GradlePlugin buildPlugin(GeneratorContext generatorContext) {
        GradlePlugin.Builder plugin = GradlePlugin.builder()
                .gradleFile(GradleFile.SETTINGS)
                .id(BUILDLESS_PLUGIN_ID)
                .lookupArtifactId(BUILDLESS_PLUGIN_ARTIFACT)
                .settingsExtension(new RockerWritable(buildlessGradlePlugin.template()));

        return plugin.build();
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.getBuildProperties().put("org.gradle.caching", "true");
            generatorContext.addBuildPlugin(buildPlugin(generatorContext));
        }
    }
}

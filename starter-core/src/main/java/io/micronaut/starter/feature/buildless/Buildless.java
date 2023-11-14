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
import io.micronaut.starter.feature.build.BuildCacheConfiguration;
import io.micronaut.starter.feature.build.gradle.templates.buildlessGradlePlugin;
import io.micronaut.starter.feature.build.gradle.templates.gradleBuildCache;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

@Singleton
public class Buildless implements CommunityFeature, BuildCacheConfiguration {
    public static final String NAME = "buildless";
    public static final boolean BUILDLESS_ENABLE_PLUGIN = true;
    public static final String FEATURE_NAME_BUILDLESS = "buildless";
    public static final String BUILDLESS_PLUGIN_ID = "build.less";
    public static final String BUILDLESS_PLUGIN_ARTIFACT = "buildless-plugin-gradle";

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

    @Override
    public String getRemoteCacheType() {
        return "HttpBuildCache";
    }

    @Override
    public boolean isEnableRemoteCache() {
        return true;
    }

    @Override
    public boolean isRemoteCachePushEnabled() {
        return true;
    }

    @Override
    public String getRemoteCacheUri() {
        return "https://gradle.less.build/cache/generic/";
    }

    @Override
    public boolean isUseExpectContinue() {
        return true;
    }

    @Override
    public boolean isUseCustomCachePlugin() {
        return BUILDLESS_ENABLE_PLUGIN;
    }

    protected GradlePlugin gradlePlugin(Buildless configuration) {
        var builder = GradlePlugin.builder()
                .gradleFile(GradleFile.SETTINGS)
                .id(BUILDLESS_PLUGIN_ID)
                .lookupArtifactId(BUILDLESS_PLUGIN_ARTIFACT);

        if (!isUseCustomCachePlugin()) {
            // use a generic HTTPS endpoint for caching
            builder.settingsExtension(new RockerWritable(gradleBuildCache.template(configuration)));
        } else {
            // use the buildless plugin
            builder.settingsImports("build.less.plugin.settings.buildless");
            builder.settingsExtension(new RockerWritable(buildlessGradlePlugin.template(configuration)));
        }

        return builder.build();
    }

    protected void applyMaven(GeneratorContext context, Buildless configuration) {

    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.getBuildProperties().put("org.gradle.caching", "true");
            generatorContext.addBuildPlugin(gradlePlugin(this));
        } else {
            applyMaven(generatorContext, this);
        }
    }
}

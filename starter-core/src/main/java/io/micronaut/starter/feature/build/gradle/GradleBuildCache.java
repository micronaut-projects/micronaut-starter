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
package io.micronaut.starter.feature.build.gradle;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.gradle.GradleDsl;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.build.gradle.templates.buildCache;
import io.micronaut.starter.template.RockerWritable;

public abstract class GradleBuildCache implements Feature, BuildRemoteCacheConfiguration {

    protected final BuildCacheCredentialsProvider buildCacheCredentialsProvider;

    public GradleBuildCache(@Nullable BuildCacheCredentialsProvider buildCacheCredentialsProvider) {
        this.buildCacheCredentialsProvider = buildCacheCredentialsProvider;
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Configures Build Cache for Micronaut Gradle Enterprise";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DEV_TOOLS;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.gradle.org/current/userguide/build_cache.html#sec:build_cache_configure";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.getBuildTool().isGradle()) {
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .settingsExtension(new RockerWritable(buildCache.template(generatorContext.getBuildTool().getGradleDsl().orElse(GradleDsl.GROOVY), this)))
                    .build());
        }
    }

    @Override
    @Nullable
    public Credentials getCredentials() {
        return buildCacheCredentialsProvider != null ? buildCacheCredentialsProvider.provideCredentails() : null;
    }
}

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
package io.micronaut.starter.feature.other;

import io.micronaut.context.annotation.Requires;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.JvmFeature;
import io.micronaut.starter.options.BuildTool;
import jakarta.annotation.Nullable;
import jakarta.inject.Singleton;

import static io.micronaut.core.util.StringUtils.TRUE;
import static io.micronaut.starter.feature.Category.VALIDATION;

@Requires(property = "micronaut.starter.feature.nullaway.enabled", value = TRUE, defaultValue = TRUE)
@Singleton
public class NullAway implements JvmFeature {

    public static final String NAME = "nullaway";
    private final Jspecify jspecify;
    private final NullAwayGradlePluginFeature nullAwayGradlePluginFeature;
    private final NullAwayMavenPluginFeature nullAwayMavenPluginFeature;

    public NullAway(Jspecify jspecify,
                    NullAwayGradlePluginFeature nullAwayGradlePluginFeature,
                    NullAwayMavenPluginFeature nullAwayMavenPluginFeature) {
        this.jspecify = jspecify;
        this.nullAwayGradlePluginFeature = nullAwayGradlePluginFeature;
        this.nullAwayMavenPluginFeature = nullAwayMavenPluginFeature;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(Jspecify.class)) {
            featureContext.addFeature(jspecify);
        }
        BuildTool buildTool = featureContext.getBuildTool();
        if (buildTool.isGradle()) {
            featureContext.addFeatureIfNotPresent(NullAwayGradlePluginFeature.class, nullAwayGradlePluginFeature);
        } else if (buildTool == BuildTool.MAVEN) {
            featureContext.addFeatureIfNotPresent(NullAwayMavenPluginFeature.class, nullAwayMavenPluginFeature);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "NullAway Annotation";
    }

    @Override
    public String getDescription() {
        return "NullAway: Fast Annotation-Based Null Checking for Java.";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://github.com/uber/NullAway/wiki";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return VALIDATION;
    }

    static Dependency.Builder nullawayDependency() {
        return Dependency.builder()
                .groupId("com.uber.nullaway")
                .lookupArtifactId("nullaway");
    }

    static Dependency.Builder errorProneDependency() {
        return Dependency.builder()
                .groupId("com.google.errorprone")
                .lookupArtifactId("error_prone_core");
    }
}

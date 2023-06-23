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
package io.micronaut.starter.feature.community;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import jakarta.inject.Singleton;

@Singleton
public class JobRunr implements JobRunrCommunityFeature {

    public static final String NAME = "jobrunr";

    private static final Dependency.Builder DEPENDENCY_JOBRUNR = Dependency.builder()
            .lookupArtifactId("jobrunr")
            .test();

    @Override
    public @NonNull String getCommunityFeatureTitle() {
        return "JobRunr";
    }

    @Override
    public @NonNull
    String getCommunityFeatureName() {
        return NAME;
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds JobRunr Micronaut integration. Jobrunr is a background processing library for Java.";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_JOBRUNR);
    }

    @Override
    public @Nullable String getThirdPartyDocumentation() {
        return "https://www.jobrunr.io/en/documentation/configuration/micronaut/";
    }
}

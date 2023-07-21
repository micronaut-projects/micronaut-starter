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
package io.micronaut.starter.feature.jobrunr;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.CommunityFeature;
import jakarta.inject.Singleton;

@Singleton
public class JobRunrFeature implements CommunityFeature {
    public static final String NAME = "jobrunr-jobrunr";

    private static final String JOBRUNR_ARTIFACT_ID = "jobrunr-micronaut-feature";

    private static final Dependency JOBRUNR_DEPENDENCY = Dependency.builder()
            .lookupArtifactId(JOBRUNR_ARTIFACT_ID)
            .compile()
            .build();

    @Override
    public String getCommunityFeatureTitle() {
        return "JobRunr Integration for Micronaut";
    }

    @Override
    public String getCommunityFeatureName() {
        return "jobrunr";
    }

    @Override
    public String getCommunityContributor() {
        return "JobRunr";
    }

    @Override
    @Nullable
    public String getDescription() {
        return "Micronaut integration for JobRunr background processing in Java";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.JOB_PROCESSING;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(JOBRUNR_DEPENDENCY);
        generatorContext.getConfiguration().put("jobrunr.background-job-server.enabled", false);
        generatorContext.getConfiguration().put("jobrunr.dashboard.enabled", false);
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://www.jobrunr.io/en/documentation/configuration/micronaut/";
    }
}

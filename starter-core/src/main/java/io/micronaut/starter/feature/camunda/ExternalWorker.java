/*
 * Copyright 2017-2022 original authors
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
package io.micronaut.starter.feature.camunda;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import jakarta.inject.Singleton;

@Singleton
public class ExternalWorker implements CamundaCommunityFeature {

    @NonNull
    @Override
    public String getCommunityFeatureName() {
        return "external-worker";
    }

    @Override
    @NonNull
    public String getCommunityFeatureTitle() {
        return "Camunda Workflow Engine External Worker";
    }

    @Override
    public String getDescription() {
        return "Bringing process automation to Micronaut: Implement Camunda External Workers for the Camunda Platform";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("camunda.external-client.base-url", "http://localhost:8080/engine-rest");
        generatorContext.addDependency(Dependency.builder()
                .lookupArtifactId("micronaut-camunda-external-client-feature")
                .compile());
    }

    @Override
    public String getCategory() {
        return Category.BPM;
    }

    @Override
    @Nullable
    public String getThirdPartyDocumentation() {
        return "https://github.com/camunda-community-hub/micronaut-camunda-external-client";
    }

}

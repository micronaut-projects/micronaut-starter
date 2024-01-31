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
package io.micronaut.starter.feature.other;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.*;
import io.micronaut.starter.feature.github.workflows.WorkflowsUtils;
import io.micronaut.starter.feature.other.template.openApiProperties;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import io.micronaut.starter.feature.staticResources.ContributingStaticResources;
import io.micronaut.starter.feature.staticResources.StaticResource;
import io.micronaut.starter.template.RockerTemplate;

import java.util.*;

abstract class OpenApiView implements Feature, MicronautServerDependent, ContributingInterceptUrlMapFeature, ContributingStaticResources {
    private final OpenApi openApiFeature;

    OpenApiView(OpenApi openApiFeature) {
        this.openApiFeature = openApiFeature;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addTemplate("openapiProperties", new RockerTemplate("openapi.properties", openApiProperties.template(this)));
        generatorContext.addTemplate("exampleController", WorkflowsUtils.createExampleController(
                generatorContext.getProject(), generatorContext.getLanguage()));
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeatureIfNotPresent(OpenApi.class, openApiFeature);
    }

    @Override
    public String getCategory() {
        return Category.API;
    }

    @Override
    public List<InterceptUrlMap> interceptUrlMaps() {
        return Collections.singletonList(InterceptUrlMap.anonymousAcccess("/swagger/**"));
    }

    @Override
    public List<StaticResource> staticResources() {
        return Collections.singletonList(new StaticResource("swagger", "/swagger/**", "classpath:META-INF/swagger"));
    }
}

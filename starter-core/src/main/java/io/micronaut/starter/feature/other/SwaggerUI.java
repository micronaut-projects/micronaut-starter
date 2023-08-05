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
package io.micronaut.starter.feature.other;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.github.workflows.WorkflowsUtils;
import io.micronaut.starter.feature.other.template.openApiProperties;
import io.micronaut.starter.feature.security.Security;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import io.micronaut.starter.template.RockerTemplate;

import jakarta.inject.Singleton;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class SwaggerUI implements Feature, MicronautServerDependent {
    public static final String NAME = "swagger-ui";

    private final OpenApi openApiFeature;

    public SwaggerUI(OpenApi openApiFeature) {
        this.openApiFeature = openApiFeature;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Swagger UI";
    }

    @Override
    public String getDescription() {
        return "Adds and enables Swagger UI by default";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addTemplate("openapiProperties", new RockerTemplate("openapi.properties", openApiProperties.template()));
        generatorContext.getConfiguration().put("micronaut.router.static-resources.swagger.paths", "classpath:META-INF/swagger");
        generatorContext.getConfiguration().put("micronaut.router.static-resources.swagger.mapping", "/swagger/**");
        generatorContext.getConfiguration().put("micronaut.router.static-resources.swagger-ui.paths", "classpath:META-INF/swagger/views/swagger-ui");
        generatorContext.getConfiguration().put("micronaut.router.static-resources.swagger-ui.mapping", "/swagger-ui/**");

        if (generatorContext.isFeaturePresent(Security.class)) {
            Map<String, String> swaggerAccess = new HashMap<>() {{
                put("pattern", "/swagger/**");
                put("access", "isAnonymous()");
            }};

            Map<String, String> swaggerUiAccess = new HashMap<>() {{
                put("pattern", "/swagger-ui/**");
                put("access", "isAnonymous()");
            }};

            generatorContext.getConfiguration().put("micronaut.security.intercept-url-map", Arrays.asList(swaggerAccess, swaggerUiAccess));
        }

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
    public String getThirdPartyDocumentation() {
        return "https://swagger.io/tools/swagger-ui/";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-openapi/latest/guide/index.html";
    }
}

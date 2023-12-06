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

import io.micronaut.starter.application.generator.GeneratorContext;
import jakarta.inject.Singleton;

@Singleton
public class SwaggerUI extends OpenApiView {
    public static final String NAME = "swagger-ui";

    public SwaggerUI(OpenApi openApiFeature) {
        super(openApiFeature);
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
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        generatorContext.getConfiguration().put("micronaut.router.static-resources.swagger-ui.paths", "classpath:META-INF/swagger/views/swagger-ui");
        generatorContext.getConfiguration().put("micronaut.router.static-resources.swagger-ui.mapping", "/swagger-ui/**");
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

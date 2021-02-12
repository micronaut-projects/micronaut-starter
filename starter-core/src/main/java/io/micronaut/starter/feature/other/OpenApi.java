/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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
import io.micronaut.starter.build.dependencies.DependencyContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.util.VersionInfo;

import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class OpenApi implements Feature, MicronautServerDependent {
    @Override
    public String getName() {
        return "openapi";
    }

    @Override
    public String getTitle() {
        return "OpenAPI Support";
    }

    @Override
    public String getDescription() {
        return "Adds support for OpenAPI (Swagger)";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addAnnotationProcessor("micronaut-openapi");
        generatorContext.addDependency("swagger-annotations");
    }

    @Override
    public String getCategory() {
        return Category.API;
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://www.openapis.org";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-openapi/latest/guide/index.html";
    }
}

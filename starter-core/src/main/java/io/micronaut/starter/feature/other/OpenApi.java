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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import jakarta.inject.Singleton;

import static io.micronaut.starter.build.dependencies.MicronautDependencyUtils.GROUP_ID_IO_MICRONAUT_OPENAPI;

@Singleton
public class OpenApi implements Feature, MicronautServerDependent {

    public static final String ARTIFACT_ID_MICRONAUT_OPENAPI = "micronaut-openapi";
    private static final String OPENAPI_VERSION_MAVEN_PROPERTY = "micronaut.openapi.version";

    private static final Dependency DEPENDENCY_SWAGGER_ANNOTATIONS = Dependency.builder()
            .groupId("io.swagger.core.v3")
            .artifactId("swagger-annotations")
            .compile()
            .build();

    @Override
    @NonNull
    public String getName() {
        return "openapi";
    }

    @Override
    public String getTitle() {
        return "OpenAPI Support";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for OpenAPI (Swagger)";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(micronautOpenApiProcessor(generatorContext));
        generatorContext.addDependency(DEPENDENCY_SWAGGER_ANNOTATIONS);
        if (generatorContext.getBuildTool() == BuildTool.MAVEN && generatorContext.getLanguage() == Language.GROOVY) {
            generatorContext.addDependency(MicronautDependencyUtils.openapi()
                    .artifactId(ARTIFACT_ID_MICRONAUT_OPENAPI)
                    .compile());
        }
    }

    public static Dependency.Builder micronautOpenApiProcessor(GeneratorContext generatorContext) {
        return MicronautDependencyUtils.annotationProcessor(generatorContext.getBuildTool(),
                GROUP_ID_IO_MICRONAUT_OPENAPI, ARTIFACT_ID_MICRONAUT_OPENAPI, OPENAPI_VERSION_MAVEN_PROPERTY);
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

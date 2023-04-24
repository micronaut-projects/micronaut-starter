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
package io.micronaut.starter.feature.validator;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.options.BuildTool;
import jakarta.inject.Singleton;

import static io.micronaut.starter.build.dependencies.MicronautDependencyUtils.ARTIFACT_ID_MICRONAUT_INJECT;
import static io.micronaut.starter.build.dependencies.MicronautDependencyUtils.GROUP_ID_MICRONAUT;

@Singleton
public class MicronautValidationFeature implements ValidationFeature {
    public static final String NAME = "micronaut-validation";
    public static final String ARTIFACT_ID_MICRONAUT_VALIDATION_PROCESSOR = "micronaut-validation-processor";

    private static final Dependency MICRONAUT_VALIDATION_COMPILE = MicronautDependencyUtils
            .validationDependency()
            .artifactId("micronaut-validation")
            .compile()
            .build();

    private static final Dependency DEPENDENCY_VALIDATON_API = Dependency.builder()
            .groupId("jakarta.validation")
            .artifactId("validation-api")
            .compile()
            .build();

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut Validation";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for Micronaut Validation";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-validation/latest/guide/";
    }

    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(MICRONAUT_VALIDATION_COMPILE);
        generatorContext.addDependency(DEPENDENCY_VALIDATON_API);
        generatorContext.addDependency(micronautValidationProcessor(generatorContext).build());
    }

    public static Dependency.Builder micronautValidationProcessor(GeneratorContext generatorContext) {
        Dependency.Builder builder = MicronautDependencyUtils
                .validationDependency()
                .artifactId(ARTIFACT_ID_MICRONAUT_VALIDATION_PROCESSOR)
                .annotationProcessor();

        if (generatorContext.getBuildTool().isGradle()) {
            return builder;
        } else if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            return builder
                    .versionProperty("micronaut.validation.version")
                    .exclude(Dependency.builder().groupId(GROUP_ID_MICRONAUT).artifactId(ARTIFACT_ID_MICRONAUT_INJECT).build());
        }
        throw new RuntimeException("build tool " + generatorContext.getBuildTool().getName() + " not supported");
    }
}

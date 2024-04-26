/*
 * Copyright 2017-2024 original authors
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
package io.micronaut.starter.feature.sourcegen;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.build.dependencies.Scope;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.options.Language;
import jakarta.inject.Singleton;

@Singleton
public class SourcegenJava implements Feature {

    public static final String NAME = "sourcegen-generator";

    private static final Dependency DEPENDENCY_MICRONAUT_SOURCEGEN_ANNOTATIONS = MicronautDependencyUtils.sourcegenDependency()
            .artifactId("micronaut-sourcegen-annotations")
            .scope(Scope.COMPILE)
            .build();

    private static final Dependency DEPENDENCY_MICRONAUT_SOURCEGEN_GENERATOR_JAVA = MicronautDependencyUtils.sourcegenDependency()
            .artifactId("micronaut-sourcegen-generator-java")
            .scope(Scope.ANNOTATION_PROCESSOR)
            .build();

    private static final Dependency DEPENDENCY_MICRONAUT_SOURCEGEN_GENERATOR_KOTLIN = MicronautDependencyUtils.sourcegenDependency()
            .artifactId("micronaut-sourcegen-generator-kotlin")
            .scope(Scope.ANNOTATION_PROCESSOR)
            .build();

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut source code generator for Java and Kotlin";
    }

    @Override
    public String getDescription() {
        return "Micronaut SourceGen exposes a language-neutral API for source code generation";
    }

    @Override
    public String getCategory() {
        return Category.API;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-sourcegen/latest/guide/";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
    }

    private void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_SOURCEGEN_ANNOTATIONS);
        if (generatorContext.getLanguage() == Language.JAVA) {
            generatorContext.addDependency(DEPENDENCY_MICRONAUT_SOURCEGEN_GENERATOR_JAVA);
        } else if (generatorContext.getLanguage() == Language.KOTLIN) {
            generatorContext.addDependency(DEPENDENCY_MICRONAUT_SOURCEGEN_GENERATOR_KOTLIN);
        }
    }
}

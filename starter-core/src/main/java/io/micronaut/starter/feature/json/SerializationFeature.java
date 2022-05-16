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
package io.micronaut.starter.feature.json;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.Substitution;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.options.BuildTool;
import java.util.ArrayList;
import java.util.List;

public interface SerializationFeature extends JsonFeature {
    String MICRONAUT_SERIALIZATION = "micronaut.serialization";
    String GROUP_ID_MICRONAUT_SERDE = "io.micronaut.serde";
    String ARTIFACT_ID_MICRONAUT_JACKSON_CORE = "micronaut-jackson-core";

    @Override
    default boolean isPreview() {
        return true;
    }

    @Override
    default String getCategory() {
        return Category.API;
    }

    @Override
    default String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-serialization/latest/guide/";
    }

    @Override
    default boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    default void apply(GeneratorContext generatorContext) {
        dependencies(generatorContext)
                .forEach(generatorContext::addDependency);
    }

    @NonNull
    default List<Dependency.Builder> dependencies(@NonNull GeneratorContext generatorContext) {
        List<Dependency.Builder> dependencyList = new ArrayList<>();
        dependencyList.add(serdeProcessor());
        dependencyList.add(serdeModule(generatorContext));
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            dependencyList.add(micronautRuntimeDependency());
        }
        return dependencyList;
    }

    @NonNull
    default Dependency.Builder micronautRuntimeDependency() {
        return Dependency.builder()
                .compile()
                .groupId(GROUP_ID_MICRONAUT)
                .artifactId("micronaut-runtime")
                .exclude(DEPENDENCY_MICRONAUT_JACKSON_DATABIND);
    }

    @NonNull
    default Dependency.Builder serdeModule(@NonNull GeneratorContext generatorContext) {
        Dependency.Builder builder = Dependency.builder()
                .compile()
                .groupId(GROUP_ID_MICRONAUT_SERDE)
                .artifactId("micronaut-serde-" + getModule());
        substitutions(generatorContext).forEach(builder::substitution);
        return builder;
    }

    @NonNull
    default Dependency.Builder serdeProcessor() {
        return Dependency.builder()
                .annotationProcessor()
                .groupId(GROUP_ID_MICRONAUT_SERDE)
                .artifactId("micronaut-serde-processor")
                .versionProperty("micronaut.serialization.version");
    }

    String getModule();

    @NonNull
    List<Substitution> substitutions(@NonNull GeneratorContext generatorContext);
}

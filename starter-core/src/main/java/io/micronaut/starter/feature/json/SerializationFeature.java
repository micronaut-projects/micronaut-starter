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

import java.util.List;

public interface SerializationFeature extends JsonFeature {
    String MICRONAUT_SERIALIZATION = "micronaut.serialization";

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
        generatorContext.addDependency(Dependency.builder()
            .annotationProcessor()
            .groupId("io.micronaut.serde")
            .artifactId("micronaut-serde-processor")
            .versionProperty("micronaut.serialization.version")
            .build()
        );
        Dependency.Builder builder = Dependency.builder()
                .compile()
                .groupId("io.micronaut.serde")
                .artifactId("micronaut-serde-" + getModule());
        substitutions(generatorContext).forEach(builder::substitution);
        generatorContext.addDependency(builder.build());
         if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
             generatorContext.addDependency(Dependency.builder()
                     .compile()
                     .groupId("io.micronaut")
                     .artifactId("micronaut-runtime")
                     .exclude(Dependency.builder()
                             .groupId("io.micronaut")
                             .artifactId("micronaut-jackson-databind")
                             .build())
             );
         }
    }

    String getModule();

    @NonNull
    List<Substitution> substitutions(@NonNull GeneratorContext generatorContext);
}

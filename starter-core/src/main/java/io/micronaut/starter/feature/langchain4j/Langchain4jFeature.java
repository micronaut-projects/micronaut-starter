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
package io.micronaut.starter.feature.langchain4j;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Feature;

public interface Langchain4jFeature extends Feature {
    String ARTIFACT_ID_MICRONAUT_LANGCHAIN4J_PROCESSOR = "micronaut-langchain4j-processor";
    Dependency DEPENDENCY_MICRONAUT_LANGCHAIN4J_PROCESSOR = MicronautDependencyUtils.langchain4j()
            .artifactId(ARTIFACT_ID_MICRONAUT_LANGCHAIN4J_PROCESSOR)
            .annotationProcessor()
            .build();

    @Override
    default void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
    }

    default void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_LANGCHAIN4J_PROCESSOR);
    }
}

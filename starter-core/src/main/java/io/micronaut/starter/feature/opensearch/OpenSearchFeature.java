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
package io.micronaut.starter.feature.opensearch;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;

public interface OpenSearchFeature extends Feature {

    @Override
    default boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    default String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-opensearch/latest/guide/";
    }

    @Override
    default String getThirdPartyDocumentation() {
        return "https://opensearch.org/docs/latest/clients/java/";
    }

    @Override
    default String getCategory() {
        return Category.SEARCH;
    }

    @Override
    default void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils
                .opensearchDependency()
                .artifactId("micronaut-" + getName())
                .compile());
    }
}

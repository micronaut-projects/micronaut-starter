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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;

public interface Langchain4jEmbeddedStore extends Langchain4jFeature {

    @Override
    default String getTitle() {
        return " Langchain4j Embedded Store";
    }

    @Override
    default String getDescription() {
        return "Integration with " + getTitle() + " Embedded Store.";
    }

    @Override
    default boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    default String getCategory() {
        return Category.EMBEDDED_STORE;
    }

    @Override
    default void addDependencies(GeneratorContext generatorContext) {
        Langchain4jFeature.super.addDependencies(generatorContext);
    }
}

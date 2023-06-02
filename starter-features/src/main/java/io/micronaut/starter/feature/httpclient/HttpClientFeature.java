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
package io.micronaut.starter.feature.httpclient;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.OneOfFeature;

import java.util.List;

public interface HttpClientFeature  extends OneOfFeature {
    @Override
    default Class<?> getFeatureClass() {
        return HttpClientFeature.class;
    }

    @Override
    default boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    default String getCategory() {
        return Category.CLIENT;
    }

    @Override
    default void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
    }

    default void addDependencies(GeneratorContext generatorContext) {
        getDependencies(generatorContext).forEach(generatorContext::addDependency);
    }

    @NonNull
    List<Dependency> getDependencies(@NonNull GeneratorContext generatorContext);
}

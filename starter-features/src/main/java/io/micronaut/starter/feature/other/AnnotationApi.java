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
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class AnnotationApi implements Feature {
    private static final Dependency DEPENDENCY_JAKARTA_ANNOTATON_API = Dependency.builder()
            .groupId("jakarta.annotation")
            .artifactId("jakarta.annotation-api")
            .compile()
            .build();

    @Override
    public String getName() {
        return "annotation-api";
    }

    @Override
    public String getTitle() {
        return "Jakarta Annotations API";
    }

    @Override
    public String getCategory() {
        return Category.API;
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds Jakarta annotations API dependency. For example, to use @PostConstruct or @PreDestroy";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://jakarta.ee/specifications/annotations/";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_JAKARTA_ANNOTATON_API);
    }
}

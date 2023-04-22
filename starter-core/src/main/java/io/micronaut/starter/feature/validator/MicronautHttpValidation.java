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
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class MicronautHttpValidation implements Feature {
    private static final Dependency DEPENDENCY_MICRONAUT_HTTP_VALIDATION = MicronautDependencyUtils.coreDependency()
            .artifactId("micronaut-http-validation")
            .versionProperty("micronaut.version")
            .annotationProcessor()
            .build();

    @Override
    public String getName() {
        return "micronaut-http-validation";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public String getTitle() {
        return "Micronaut HTTP Validation";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for validating route arguments at compile time";
    }

    @Override
    public String getCategory() {
        return Category.VALIDATION;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
    }

    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_HTTP_VALIDATION);
    }
}

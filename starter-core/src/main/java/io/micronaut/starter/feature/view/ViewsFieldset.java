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
package io.micronaut.starter.feature.view;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class ViewsFieldset implements Feature {
    public static final String NAME = "views-fieldset";
    private static final String ARTIFACT_ID_MICRONAUT_VIEWS_FIELDSET = "micronaut-views-fieldset";
    private static final Dependency DEPENDENCY_VIEWS_FIELDSET =
            MicronautDependencyUtils.viewsDependency().artifactId(ARTIFACT_ID_MICRONAUT_VIEWS_FIELDSET)
                    .compile()
                    .build();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Form & Fieldset Generator";
    }

    @Override
    public String getDescription() {
        return "Adds the views-fieldset dependency, which provides an API to simplify the generation of an HTML Fieldset representation for a given type or instance.";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.DEFAULT;
    }

    @Override
    public String getCategory() {
        return Category.VIEW;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
    }

    private void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_VIEWS_FIELDSET);
    }
}

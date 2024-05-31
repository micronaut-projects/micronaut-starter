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
package io.micronaut.starter.feature.guice;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class MicronautGuice implements Feature {
    public static final String NAME = "guice";
    private static final String ARTIFACT_ID_MICRONAUT_GUICE = "micronaut-guice";
    private static final String ARTIFACT_ID_MICRONAUT_GUICE_PROCESSOR = "micronaut-guice-processor";
    private static final Dependency MICRONAUT_GUICE_ANNOTATION_PROCESSOR = MicronautDependencyUtils.guiceDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_GUICE_PROCESSOR)
            .annotationProcessor()
            .build();
    private static final Dependency MICRONAUT_GUICE = MicronautDependencyUtils.guiceDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_GUICE)
            .compile()
            .build();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut Guice";
    }

    @Override
    public String getDescription() {
        return "Micronaut Guice allows importing Guice modules into a Micronaut application, making the Guice bindings available for Dependency Injection.";
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
        generatorContext.addDependency(MICRONAUT_GUICE);
        generatorContext.addDependency(MICRONAUT_GUICE_ANNOTATION_PROCESSOR);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-guice/latest/guide/index.html";
    }

    @Override
    public String getCategory() {
        return Category.DI;
    }
}

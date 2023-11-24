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
package io.micronaut.starter.feature.eclipsestore;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import jakarta.inject.Singleton;

@Singleton
public class EclipseStore implements EclipseStoreFeature {

    public static final String NAME = "eclipsestore";
    public static final String MICRONAUT_ECLIPSESTORE_ANNOTATIONS_ARTIFACT = "micronaut-eclipsestore-annotations";
    public static final String MICRONAUT_ECLIPSESTORE_VERSION = "micronaut.eclipsestore.version";
    public static final String ARTIFACT_ID_MICRONAUT_ECLIPSESTORE = "micronaut-eclipsestore";
    public static final Dependency DEPENDENCY_MICRONAUT_ECLIPSE_STORE = MicronautDependencyUtils.eclipsestoreDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_ECLIPSESTORE)
            .compile()
            .build();

    public static final Dependency DEPENDENCY_MICRONAUT_ECLIPSE_STORE_ANNOTATIONS = MicronautDependencyUtils.eclipsestoreDependency()
            .artifactId(MICRONAUT_ECLIPSESTORE_ANNOTATIONS_ARTIFACT)
            .compile()
            .build();

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "EclipseStore";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for using EclipseStore with Micronaut";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_ECLIPSE_STORE);
        generatorContext.addDependency(MicronautDependencyUtils.annotationProcessor(generatorContext.getBuildTool(),
                MicronautDependencyUtils.GROUP_ID_MICRONAUT_ECLIPSESTORE, MICRONAUT_ECLIPSESTORE_ANNOTATIONS_ARTIFACT, MICRONAUT_ECLIPSESTORE_VERSION));
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_ECLIPSE_STORE_ANNOTATIONS);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-eclipsestore/latest/guide";
    }

    @Override
    @Nullable
    public String getThirdPartyDocumentation() {
        return "https://docs.eclipsestore.io/";
    }
}

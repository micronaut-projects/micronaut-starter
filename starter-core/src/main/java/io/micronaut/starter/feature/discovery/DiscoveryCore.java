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
package io.micronaut.starter.feature.discovery;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

@Singleton
public class DiscoveryCore implements Feature {
    private static final String ARTIFACT_ID_MICRONAUT_DISCOVERY_CORE = "micronaut-discovery-core";
    private static final Dependency DEPENDENCY_MICRONAUT_DISCOVERY_CORE = MicronautDependencyUtils.coreDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_DISCOVERY_CORE)
            .compile()
            .build();

    @NonNull
    @Override
    public String getName() {
        return "discovery-core";
    }

    @Override
    public String getTitle() {
        return "Micronaut Discovery Core";
    }

    @Override
    public String getDescription() {
        return "Adds micronaut-discovery-core dependency for base service discovery features.";
    }

    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_DISCOVERY_CORE);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-discovery-client/latest/guide/";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType != ApplicationType.CLI;
    }

    @Override
    public String getCategory() {
        return Category.SERVICE_DISCOVERY;
    }
}

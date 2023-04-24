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
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import jakarta.inject.Singleton;

@Singleton
public class DiscoveryCore implements DiscoveryFeature {
    protected static final Dependency DEPENDENCY_MICRONAUT_DISCOVERY_CLIENT = Dependency.builder()
            .groupId("io.micronaut.discovery")
            .artifactId("micronaut-discovery-client")
            .compile()
            .build();

    private static final Dependency DEPENDENCY_MICRONAUT_DISCOVERY_CORE = MicronautDependencyUtils.coreDependency()
            .artifactId("micronaut-discovery-core")
            .compile()
            .build();

    @NonNull
    @Override
    public String getName() {
        return "discovery-core";
    }

    @Override
    public String getTitle() {
        return "Micronaut Service Discovery";
    }

    @Override
    public String getDescription() {
        return "Adds support for Service Discovery with Micronaut";
    }

    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_DISCOVERY_CORE);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-discovery-client/latest/guide/";
    }
}

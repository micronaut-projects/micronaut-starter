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
package io.micronaut.starter.feature.json;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.build.dependencies.Substitution;
import io.micronaut.starter.feature.testresources.TestResources;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Singleton;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Singleton
public class SerializationJsonpFeature implements SerializationFeature {
    private static final String ARTIFACT_ID_MICRONAUT_SERDE_JSONP = "micronaut-serde-jsonp";

    @Override
    public String getName() {
        return "serialization-jsonp";
    }

    @Override
    public String getDescription() {
        return "Adds support using Micronaut Serialization with JSON-B and JSON-P";
    }

    @Override
    public String getTitle() {
        return "Micronaut Serialization JSON-B and JSON-P";
    }

    @Override
    public String getModule() {
        return "jsonp";
    }

    @NonNull
    @Override
    public List<Dependency.Builder> dependencies(@NonNull GeneratorContext generatorContext) {
        List<Dependency.Builder> dependencyList = SerializationFeature.super.dependencies(generatorContext);
        if (generatorContext.getBuildTool() == BuildTool.MAVEN) {
            dependencyList.add(Dependency.builder()
                    .lookupArtifactId("jakarta.json.bind-api")
                    .compile());
        }
        return dependencyList;
    }

    @Override
    @NonNull
    public List<Substitution> substitutions(@NonNull GeneratorContext generatorContext) {
        // See https://github.com/micronaut-projects/micronaut-test-resources/issues/103
        if (generatorContext.isFeaturePresent(TestResources.class)) {
            return Collections.emptyList();
        }

        String serializationVersion = VersionInfo.getBomVersion(MICRONAUT_SERIALIZATION);
        String dataBindVersion = generatorContext.resolveCoordinate("jakarta.json.bind-api").getVersion();
        return Arrays.asList(Substitution.builder()
                        .target(DEPENDENCY_MICRONAUT_JACKSON_DATABIND)
                        .replacement(Dependency.builder()
                                .groupId("jakarta.json.bind")
                                .artifactId("jakarta.json.bind-api")
                                .version(dataBindVersion)
                                .build())
                        .build(),
                Substitution.builder()
                        .target(MicronautDependencyUtils.coreDependency()
                                .artifactId(ARTIFACT_ID_MICRONAUT_JACKSON_CORE)
                                .build())
                        .replacement(MicronautDependencyUtils.serdeDependency()
                                .artifactId(ARTIFACT_ID_MICRONAUT_SERDE_JSONP)
                                .version(serializationVersion)
                                .build())
                        .build()
                );
    }
}

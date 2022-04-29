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
import io.micronaut.starter.build.dependencies.Substitution;
import io.micronaut.starter.util.VersionInfo;
import jakarta.inject.Singleton;

import java.util.Arrays;
import java.util.List;

@Singleton
public class SerializationBsonFeature implements SerializationFeature {
    private static final String ARTIFACT_ID_MICRONAUT_SERDE_BSON = "micronaut-serde-bson";

    @Override
    public String getName() {
        return "serialization-bson";
    }

    @Override
    public String getDescription() {
        return "Adds support using Micronaut Serialization with BSON";
    }

    @Override
    public String getTitle() {
        return "Micronaut Serialization BSON";
    }

    @Override
    public String getModule() {
        return "bson";
    }

    @Override
    @NonNull
    public List<Substitution> substitutions(@NonNull GeneratorContext generatorContext) {
        String serializationVersion = VersionInfo.getBomVersion(MICRONAUT_SERIALIZATION);
        Dependency replacement = Dependency.builder()
                .groupId(GROUP_ID_MICRONAUT_SERDE)
                .artifactId(ARTIFACT_ID_MICRONAUT_SERDE_BSON)
                .version(serializationVersion)
                .build();
        return Arrays.asList(Substitution.builder()
                        .target(DEPENDENCY_MICRONAUT_JACKSON_DATABIND)
                        .replacement(replacement)
                        .build(),
                Substitution.builder()
                        .target(Dependency.builder()
                                .groupId(GROUP_ID_MICRONAUT)
                                .artifactId(ARTIFACT_ID_MICRONAUT_JACKSON_CORE)
                                .build())
                        .replacement(replacement)
                        .build()
        );
    }

}

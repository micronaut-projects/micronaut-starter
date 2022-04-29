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
public class SerializationJsonpFeature implements SerializationFeature {
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

    @Override
    @NonNull
    public List<Substitution> substitutions(@NonNull GeneratorContext generatorContext) {
        String serializationVersion = VersionInfo.getBomVersion(MICRONAUT_SERIALIZATION);
        String dataBindVersion = generatorContext.resolveCoordinate("jakarta.json.bind-api").getVersion();
        return Arrays.asList(Substitution.builder()
                        .target(Dependency.builder()
                                .groupId("io.micronaut")
                                .artifactId("micronaut-jackson-databind")
                                .build())
                        .replacement(Dependency.builder()
                                .groupId("jakarta.json.bind")
                                .artifactId("jakarta.json.bind-api")
                                .version(dataBindVersion)
                                .build())
                        .build(),
                Substitution.builder()
                        .target(Dependency.builder()
                                .groupId("io.micronaut")
                                .artifactId("micronaut-jackson-core")
                                .build())
                        .replacement(Dependency.builder()
                                .groupId("io.micronaut.serde")
                                .artifactId("micronaut-serde-jsonp")
                                .version(serializationVersion)
                                .build())
                        .build()
                );
    }
}

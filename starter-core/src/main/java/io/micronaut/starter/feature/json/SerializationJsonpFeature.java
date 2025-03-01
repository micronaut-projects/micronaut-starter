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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.buildtools.dependencies.Dependency;
import io.micronaut.projectgen.core.buildtools.BuildTool;
import io.micronaut.projectgen.core.utils.OptionUtils;
import jakarta.inject.Singleton;

import java.util.List;

@Requires(property = "micronaut.starter.feature.serialization.jsonp.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
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
        if (OptionUtils.hasMavenBuildTool(generatorContext.getOptions())) {
            dependencyList.add(Dependency.builder()
                    .lookupArtifactId("jakarta.json.bind-api")
                    .compile());
        }
        return dependencyList;
    }
}

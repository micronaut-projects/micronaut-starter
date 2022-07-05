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
package io.micronaut.starter.feature.opentelemetry;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.other.Management;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import io.micronaut.starter.feature.tracing.TracingFeature;
import jakarta.inject.Singleton;

@Singleton
public class OpenTelemetry implements OpenTelemetryFeature {

    private static final Dependency MICRONAUT_OPEN_TELEMETRY = Dependency.builder()
            .groupId("io.micronaut.tracing")
            .artifactId("micronaut-tracing-opentelemetry")
            .compile()
            .build();

    @NonNull
    @Override
    public String getName() {
        return "tracing-opentelemetry";
    }

    @NonNull
    @Override
    public String getTitle() {
        return "Micronaut Integration with OpenTelemetry";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Enables the integration with OpenTelemetry";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MICRONAUT_OPEN_TELEMETRY);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-tracing/latest/guide/#opentelemetry";
    }
}

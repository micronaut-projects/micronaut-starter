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
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import jakarta.inject.Singleton;

@Singleton
public class OpenTelemetryGrpc implements OpenTelemetryFeature {

    private static final Dependency MICRONAUT_OPEN_TELEMETRY_GRPC = MicronautDependencyUtils.tracingDependency()
            .artifactId("micronaut-tracing-opentelemetry-grpc")
            .compile()
            .build();

    @NonNull
    @Override
    public String getName() {
        return "tracing-opentelemetry-grpc";
    }

    @NonNull
    @Override
    public String getTitle() {
        return "Micronaut Integration with OpenTelemetry and gRPC";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds Micronaut OpenTelemetry gRPC related dependencies.";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MICRONAUT_OPEN_TELEMETRY_GRPC);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return applicationType == ApplicationType.GRPC;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-tracing/latest/guide/#grpc";
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}

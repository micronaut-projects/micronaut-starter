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
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.other.Management;
import io.micronaut.starter.feature.server.MicronautServerDependent;
import io.micronaut.starter.feature.tracing.TracingFeature;
import jakarta.inject.Singleton;

import java.util.Collections;

@Singleton
public class OpenTelemetryHttp implements TracingFeature, MicronautServerDependent {

    private static final Dependency MICRONAUT_OPEN_TELEMETRY_HTTP = MicronautDependencyUtils.tracingDependency()
            .artifactId("micronaut-tracing-opentelemetry-http")
            .compile()
            .build();

    @Override
    public boolean isVisible() {
        return false;
    }

    @NonNull
    @Override
    public String getName() {
        return "tracing-opentelemetry-http";
    }

    @NonNull
    @Override
    public String getTitle() {
        return "OpenTelemetry HTTP";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Enables the creation of span objects HTTP server request, client request, server response and client response";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MICRONAUT_OPEN_TELEMETRY_HTTP);
        if (generatorContext.getFeatures().hasFeature(Management.class)) {
            generatorContext.getConfiguration().addListItem("otel.traces.exclusions", "/health");
        }
    }

    @Override
    public String getMicronautDocumentation() {
        return "http://localhost/micronaut-tracing/guide/index.html#opentelemetry";
    }
}

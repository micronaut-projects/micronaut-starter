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

import java.util.Locale;

public interface OpenTelemetryExporterFeature extends OpenTelemetryFeature {

    @NonNull
    @Override
    default String getName() {
        return "tracing-opentelemetry-exporter-" + exporterName().toLowerCase(Locale.ROOT);
    }

    @NonNull
    @Override
    default String getTitle() {
        return "OpenTelemetry Exporter " + exporterName();
    }

    @Override
    @NonNull
    default String getDescription() {
        return "Adds the open telemetry exporter depedendency for " + exporterName();
    }

    @Override
    default void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(OpenTelemetryDependencyUtils.openTelemetryDependency()
                .artifactId(exporterArtifactId())
                .compile());
        generatorContext.getConfiguration().addNested("otel.traces.exporter", exporterName().toLowerCase(Locale.ROOT));
    }

    @Override
    default String getMicronautDocumentation() {
        return "http://localhost/micronaut-tracing/guide/index.html#opentelemetry";
    }

    @NonNull
    String exporterName();

    @NonNull
    default String exporterArtifactId() {
        return "opentelemetry-exporter-"  + exporterName().toLowerCase(Locale.ROOT);
    }
}

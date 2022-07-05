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
import io.micronaut.starter.feature.FeatureContext;
import java.util.Locale;

public abstract class OpenTelemetryExporterFeature implements OpenTelemetryFeature, OpenTelemetryExporter {
    private final OpenTelemetry openTelemetry;

    public OpenTelemetryExporterFeature(OpenTelemetry openTelemetry) {
        this.openTelemetry = openTelemetry;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(OpenTelemetryHttp.class)) {
            featureContext.addFeature(openTelemetry);
        }
    }

    @NonNull
    @Override
    public String getName() {
        return "tracing-opentelemetry-exporter-" + exporterName().toLowerCase(Locale.ROOT);
    }

    @NonNull
    @Override
    public String getTitle() {
        return "OpenTelemetry Exporter " + exporterName();
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds the open telemetry exporter depedendency for " + exporterName();
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(OpenTelemetryDependencyUtils.openTelemetryDependency()
                .artifactId(exporterArtifactId())
                .compile());
        generatorContext.getConfiguration().addNested("otel.traces.exporter", exporterName().toLowerCase(Locale.ROOT));
    }

    @Override
    public String getMicronautDocumentation() {
        return "http://localhost/micronaut-tracing/guide/index.html#opentelemetry";
    }

    @NonNull
    protected abstract String exporterName();

    @NonNull
    protected String exporterArtifactId() {
        return "opentelemetry-exporter-"  + exporterName().toLowerCase(Locale.ROOT);
    }
}

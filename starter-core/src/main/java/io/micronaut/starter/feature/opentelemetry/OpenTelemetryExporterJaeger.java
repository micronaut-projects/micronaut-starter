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
import jakarta.inject.Singleton;

import java.util.Locale;

@Singleton
public class OpenTelemetryExporterJaeger extends OpenTelemetryExporterFeature {
    private static final String EXPORTER_JAEGER = "Jaeger";

    @NonNull
    @Override
    public String getName() {
        return "tracing-opentelemetry-exporter-" + EXPORTER_JAEGER.toLowerCase(Locale.ROOT);
    }

    @NonNull
    @Override
    public String getTitle() {
        return "OpenTelemetry Exporter " + EXPORTER_JAEGER;
    }

    @NonNull
    public String exporterName() {
        return OpenTelemetryExporterOtlp.EXPORTER_OTLP;
    }

    @Override
    protected void addConfiguration(GeneratorContext generatorContext) {
        super.addConfiguration(generatorContext);
        generatorContext.getConfiguration().put("otel.exporter.otlp.endpoint", "http://localhost:4317");
    }
}

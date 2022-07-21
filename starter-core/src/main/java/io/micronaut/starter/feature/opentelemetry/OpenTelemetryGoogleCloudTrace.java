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
import jakarta.inject.Singleton;

@Singleton
public class OpenTelemetryGoogleCloudTrace extends AbstractOpenTelemetry {
    public OpenTelemetryGoogleCloudTrace(OpenTelemetry otel,
                                         OpenTelemetryHttp otelHttp,
                                         OpenTelemetryAnnotations otelAnnotations,
                                         OpenTelemetryGrpc openTelemetryGrpc,
                                         OpenTelemetryExporterGoogleCloudTrace otelExporter) {
        super(otel, otelHttp, otelAnnotations, openTelemetryGrpc, otelExporter);
    }

    @Override
    @NonNull
    public String getName() {
        return super.getName() + "gcp";
    }

    @Override
    public String getTitle() {
        return "OpenTelemetry Google Cloud Trace";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "It adds Micronaut integration with OpenTelemetry and sets Google Cloud Trace as the exporter.";
    }
}

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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.FeatureContext;

public class AbstractOpenTelemetry implements OpenTelemetryFeature {
    private final OpenTelemetry otel;
    private final OpenTelemetryHttp otelHttp;
    private final OpenTelemetryAnnotations otelAnnotations;
    private final OpenTelemetryGrpc openTelemetryGrpc;
    private final OpenTelemetryExporterFeature otelExporter;

    public AbstractOpenTelemetry(OpenTelemetry otel,
                                 OpenTelemetryHttp otelHttp,
                                 OpenTelemetryAnnotations otelAnnotations,
                                 OpenTelemetryGrpc openTelemetryGrpc,
                                 OpenTelemetryExporterFeature otelExporter) {
        this.otel = otel;
        this.otelHttp = otelHttp;
        this.otelAnnotations = otelAnnotations;
        this.openTelemetryGrpc = openTelemetryGrpc;
        this.otelExporter = otelExporter;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(OpenTelemetryAnnotations.class)) {
            featureContext.addFeature(otelAnnotations);
        }
        if (featureContext.getApplicationType() == ApplicationType.DEFAULT) {
            if (!featureContext.isPresent(OpenTelemetryHttp.class)) {
                featureContext.addFeature(otelHttp);
            }
        } else if (featureContext.getApplicationType() == ApplicationType.GRPC) {
            if (!featureContext.isPresent(OpenTelemetryGrpc.class)) {
                featureContext.addFeature(openTelemetryGrpc);
            }
        } else {
            if (!featureContext.isPresent(OpenTelemetry.class)) {
                featureContext.addFeature(otel);
            }
        }

        if (!featureContext.isPresent(otelExporter.getClass())) {
            featureContext.addFeature(otelExporter);
        }
    }
}

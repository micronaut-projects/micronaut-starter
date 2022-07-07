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
import io.micronaut.starter.feature.gcp.GcpFeature;
import jakarta.inject.Singleton;

@Singleton
public class OpenTelemetryExporterGoogleCloudTrace extends OpenTelemetryExporterFeature {

    private static final Dependency DEPENDENCY_OTEL_EXPORTER_GOOGLE_CLOUD_TRACE =
            Dependency.builder()
                    .groupId("com.google.cloud.opentelemetry")
                    .artifactId("exporter-auto")
                    .compile()
                    .build();
    private static final String GOOGLE_CLOUD_TRACE = "google_cloud_trace";

    public OpenTelemetryExporterGoogleCloudTrace(OpenTelemetry openTelemetry) {
        super(openTelemetry);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        super.apply(generatorContext);
        GcpFeature.getGraalVMDependencyIfNecessary(generatorContext).ifPresent(generatorContext::addDependency);
        GcpFeature.getGraalVMProfileIfNecessary(generatorContext).ifPresent(generatorContext::addProfile);
    }

    @NonNull
    protected Dependency exporterDependency() {
        return DEPENDENCY_OTEL_EXPORTER_GOOGLE_CLOUD_TRACE;
    }

    @Override
    @NonNull
    protected String exporterName() {
        return "gcp";
    }

    @Override
    @NonNull
    protected String exporterValue() {
        return GOOGLE_CLOUD_TRACE;
    }
}

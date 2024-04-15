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
import io.micronaut.starter.feature.aws.AwsV2Sdk;
import jakarta.inject.Singleton;

@Singleton
public class OpenTelemetryXray extends AbstractOpenTelemetry {
    public static final String NAME = "tracing-opentelemetry-xray";
    private static final Dependency.Builder OPEN_TELEMETRY_INSTRUMENTATION_AWS_SDK =
            OpenTelemetryDependencyUtils.openTelemetryInstrumentationDependency()
                    .artifactId("opentelemetry-aws-sdk-2.2")
                    .compile();
    private static final String GROUP_ID_IO_OPENTELEMETRY_CONTRIB = "io.opentelemetry.contrib";
    private static final Dependency OPEN_TELEMETRY_CONTRIB_XRAY = Dependency.builder()
            .groupId(GROUP_ID_IO_OPENTELEMETRY_CONTRIB)
            .artifactId("opentelemetry-aws-xray")
            .compile()
            .build();
    private static final String ARTIFACT_ID_OPENTELEMETRY_AWS_RESOURCES = "opentelemetry-aws-resources";
    private static final String ARTIFACT_ID_OPENTELEMETRY_AWS_XRAY_PROPAGATOR = "opentelemetry-aws-xray-propagator";
    private static final Dependency OPEN_TELEMETRY_CONTRIB_XRAY_RESOURCES = Dependency.builder()
            .groupId(GROUP_ID_IO_OPENTELEMETRY_CONTRIB)
            .artifactId(ARTIFACT_ID_OPENTELEMETRY_AWS_RESOURCES)
            .compile()
            .build();
    private static final Dependency OPEN_TELEMETRY_CONTRIB_XRAY_PROPAGATOR = Dependency.builder()
            .groupId(GROUP_ID_IO_OPENTELEMETRY_CONTRIB)
            .artifactId(ARTIFACT_ID_OPENTELEMETRY_AWS_XRAY_PROPAGATOR)
            .version("1.34.0-alpha") //TODO remove once it is managed https://github.com/micronaut-projects/micronaut-tracing/pull/541
            .compile()
            .build();
    private static final Dependency.Builder OPEN_TELEMETRY_BOM_ALPHA = Dependency.builder()
            .lookupArtifactId("opentelemetry-instrumentation-bom-alpha")
            .compile();

    public OpenTelemetryXray(OpenTelemetry otel,
                             OpenTelemetryHttp otelHttp,
                             OpenTelemetryAnnotations otelAnnotations,
                             OpenTelemetryGrpc openTelemetryGrpc,
                             OpenTelemetryExporterOtlp otelExporter) {
        super(otel, otelHttp, otelAnnotations, openTelemetryGrpc, otelExporter);
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "OpenTelemetry XRay Tracing";
    }

    @NonNull
    @Override
    public String getDescription() {
        return "Adds support for distributed tracing with XRay via Open Telemetry";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(OPEN_TELEMETRY_CONTRIB_XRAY);
        generatorContext.addDependency(OPEN_TELEMETRY_CONTRIB_XRAY_RESOURCES);
        generatorContext.addDependency(OPEN_TELEMETRY_CONTRIB_XRAY_PROPAGATOR);
        if (generatorContext.getFeatures().isFeaturePresent(AwsV2Sdk.class)) {
            generatorContext.addDependency(OPEN_TELEMETRY_BOM_ALPHA);
            generatorContext.addDependency(OPEN_TELEMETRY_INSTRUMENTATION_AWS_SDK);
        }
        generatorContext.getConfiguration().addCommaSeparatedValue("otel.traces.propagator", "tracecontext, baggage, xray");
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://docs.aws.amazon.com/xray/latest/devguide/aws-xray.html";
    }
}

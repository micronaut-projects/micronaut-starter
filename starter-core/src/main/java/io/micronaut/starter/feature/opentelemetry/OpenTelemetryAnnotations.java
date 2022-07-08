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
import jakarta.inject.Singleton;

@Singleton
public class OpenTelemetryAnnotations implements OpenTelemetryFeature {

    private static final Dependency.Builder MICRONAUT_OPEN_TELEMETRY_ANNOTATION_PROCESSOR = MicronautDependencyUtils.tracingDependency()
            .artifactId("micronaut-tracing-opentelemetry-annotation")
            .versionProperty("micronaut.tracing.version")
            .annotationProcessor();
    
    @Override
    public boolean isVisible() {
        return false;
    }
    
    @NonNull
    @Override
    public String getName() {
        return "tracing-opentelemetry-annotations";
    }

    @NonNull
    @Override
    public String getTitle() {
        return "OpenTelemetry Annotations";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Enables usage of Open Telemetry annotations.";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MICRONAUT_OPEN_TELEMETRY_ANNOTATION_PROCESSOR);
    }
}

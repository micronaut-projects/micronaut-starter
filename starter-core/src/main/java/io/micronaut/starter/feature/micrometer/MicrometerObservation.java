/*
 * Copyright 2017-2023 original authors
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
package io.micronaut.starter.feature.micrometer;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import jakarta.inject.Singleton;

import static io.micronaut.starter.build.dependencies.MicronautDependencyUtils.ARTIFACT_ID_PREFIX_MICRONAUT_MICROMETER;

@Singleton
public class MicrometerObservation implements Feature {
    public static final String NAME = "micrometer-observation";
    public static final String ARTIFACT_ID_MICRONAUT_MICROMETER_OBSERVATION = ARTIFACT_ID_PREFIX_MICRONAUT_MICROMETER + "observation";
    public static final Dependency DEPENDENCY_MICRONAUT_MICROMETER_OBSERVATION = MicronautDependencyUtils.micrometerDependency()
            .artifactId(ARTIFACT_ID_MICRONAUT_MICROMETER_OBSERVATION)
            .compile()
            .build();
    public static final String TITLE = "Micronaut Micrometer Observation";

    @Override
    public String getTitle() {
        return TITLE;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return "Simplifies code instrumentation for gathering traces and metrics";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.METRICS;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
    }

    protected void addDependencies(@NonNull GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_MICROMETER_OBSERVATION);
    }
}

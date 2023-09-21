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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Feature;

import io.micronaut.starter.feature.database.r2dbc.R2dbcFeature;
import jakarta.inject.Singleton;

@Singleton
public class Core implements Feature {
    private static final Dependency MICRONAUT_MICROMETER_CORE = Dependency.builder()
            .groupId("io.micronaut.micrometer")
            .artifactId("micronaut-micrometer-core")
            .compile()
            .build();

    private static final Dependency R2DBC_POOL = Dependency.builder()
            .groupId("io.r2dbc")
            .artifactId("r2dbc-pool")
            .runtime()
            .build();

    @Override
    public String getName() {
        return "micrometer";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public String getTitle() {
        return "Micrometer Core";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put("micronaut.metrics.enabled", true);
        generatorContext.addDependency(MICRONAUT_MICROMETER_CORE);
        if (generatorContext.hasFeature(R2dbcFeature.class)) {
            generatorContext.addDependency(R2DBC_POOL);
        }
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }
}

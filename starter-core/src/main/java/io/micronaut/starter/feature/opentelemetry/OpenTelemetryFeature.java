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
import io.micronaut.projectgen.core.generator.GeneratorContext;
import io.micronaut.projectgen.core.options.Options;
import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.projectgen.micronaut.MicronautOptions;

import static io.micronaut.projectgen.micronaut.ApplicationType.CLI;
import static io.micronaut.starter.feature.Category.TRACING;

public interface OpenTelemetryFeature extends Feature  {
    @Override
    default boolean supports(Options options) {
        return options instanceof MicronautOptions mnOptions && mnOptions.applicationType() != ApplicationType.CLI;
    }

    @Override
    @NonNull
    default String getName() {
        return "tracing-opentelemetry-";
    }

    @Override
    default String getCategory() {
        return TRACING;
    }

    @Override
    default String getFrameworkDocumentation(GeneratorContext generatorContext) {
        return "https://micronaut-projects.github.io/micronaut-tracing/latest/guide/#opentelemetry";
    }

    @Override
    default String getThirdPartyDocumentation(GeneratorContext generatorContext) {
        return "https://opentelemetry.io";
    }
}

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
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;

import static io.micronaut.starter.application.ApplicationType.CLI;
import static io.micronaut.starter.feature.Category.TRACING;

public interface OpenTelemetryFeature extends Feature  {
    @Override
    default boolean supports(ApplicationType applicationType) {
        return applicationType != CLI;
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
    default String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-tracing/latest/guide/#opentelemetry";
    }

    @Override
    default String getThirdPartyDocumentation() {
        return "https://opentelemetry.io";
    }
}

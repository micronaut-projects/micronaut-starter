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
package io.micronaut.starter.feature.json;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.DefaultFeature;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.function.gcp.AbstractGoogleCloudFunction;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class SerializationJacksonFeature implements SerializationFeature, DefaultFeature {
    private static final String ARTIFACT_ID_MICRONAUT_SERDE_JACKSON = "micronaut-serde-jackson";

    @Override
    @NonNull
    public String getName() {
        return "serialization-jackson";
    }

    @Override
    public String getDescription() {
        return "Adds support using Micronaut Serialization with Jackson Core";
    }

    @Override
    public String getTitle() {
        return "Micronaut Serialization Jackson Core";
    }

    @Override
    public String getModule() {
        return "jackson";
    }

    @Override
    public boolean shouldApply(ApplicationType applicationType, Options options, Set<Feature> selectedFeatures) {
        return selectedFeatures.stream().noneMatch(feature ->
                feature instanceof JsonFeature || feature instanceof AbstractGoogleCloudFunction);
    }

}

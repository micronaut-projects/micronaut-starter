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
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.feature.function.gcp.AbstractGoogleCloudFunction;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;
import java.util.function.Predicate;

@Singleton
public class SerializationJacksonFeature implements SerializationFeature, DefaultFeature {

    public static final Predicate<Set<Feature>> APPLY_AS_DEFAULT_FEATURE_IF_NO_JSON_FEATURE = selectedFeatures -> selectedFeatures.stream().noneMatch(feature ->
            feature instanceof JsonFeature);

    public static final Predicate<Set<Feature>> APPLY_AS_DEFAULT_FEATURE = selectedFeatures -> selectedFeatures.stream().noneMatch(feature ->
            feature instanceof AbstractGoogleCloudFunction || feature instanceof AwsLambda);
    public static final String NAME = "serialization-jackson";

    @Override
    @NonNull
    public String getName() {
        return NAME;
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
        return APPLY_AS_DEFAULT_FEATURE_IF_NO_JSON_FEATURE.test(selectedFeatures) && APPLY_AS_DEFAULT_FEATURE.test(selectedFeatures);
    }
}

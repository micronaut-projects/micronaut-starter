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
package io.micronaut.starter.feature.function.gcp;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.graalvm.GraalVM;
import io.micronaut.starter.feature.json.SerializationFeature;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.options.JdkVersion;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class GoogleCloudFunctionFeatureValidator implements FeatureValidator {

    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        if (features.stream().anyMatch(f -> f instanceof GoogleCloudFunction || f instanceof GoogleCloudRawFunction)) {
            if (features.stream().anyMatch(f -> f instanceof GraalVM)) {
                throw new IllegalArgumentException("Google Cloud Function is not supported for GraalVM. " +
                    "Consider Google Cloud Run for deploying GraalVM native images as docker containers.");
            }
            if (features.stream().anyMatch(SerializationFeature.class::isInstance)) {
                throw new IllegalArgumentException("Google Cloud Function does not currently support micronaut-serialization.");
            }
        }
    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        if (features.stream().anyMatch(f -> f instanceof GoogleCloudFunction)) {
            if (options.getJavaVersion().majorVersion() < JdkVersion.JDK_11.majorVersion()) {
                throw new IllegalArgumentException("Google Cloud Function needs at least JDK 11");
            }
        }
    }
}

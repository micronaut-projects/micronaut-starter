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
package io.micronaut.starter.feature.database.r2dbc;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.database.TestContainers;
import io.micronaut.starter.feature.migration.MigrationFeature;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class R2dbcFeatureValidator implements FeatureValidator {

    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        if (containsR2dbcFeature(features) && hasMigrationFeature(features) && hasTestContainersFeature(features)) {
            throw new IllegalArgumentException("Testcontainers is not supported with R2DBC and Migration. Please remove the TestContainers feature to use Test Resources instead.");
        }
    }

    private static boolean hasTestContainersFeature(Set<Feature> features) {
        return features.stream().anyMatch(TestContainers.class::isInstance);
    }

    private static boolean hasMigrationFeature(Set<Feature> features) {
        return features.stream().anyMatch(f -> MigrationFeature.class.isAssignableFrom(f.getClass()));
    }

    private static boolean containsR2dbcFeature(Set<Feature> features) {
        return features.stream().anyMatch(f -> R2dbcFeature.class.isAssignableFrom(f.getClass()));
    }
}

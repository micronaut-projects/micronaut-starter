/*
 * Copyright 2017-2021 original authors
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
package io.micronaut.starter.feature.messaging.jms;

import io.micronaut.projectgen.micronaut.ApplicationType;
import io.micronaut.projectgen.core.feature.Feature;
import io.micronaut.starter.feature.database.DatabaseDriverFeature;
import io.micronaut.starter.feature.database.Oracle;
import io.micronaut.projectgen.core.feature.FeatureValidator;
import io.micronaut.projectgen.core.options.Options;
import jakarta.inject.Singleton;

import java.util.Optional;
import java.util.Set;

@Singleton
public class OracleAdvancedQueuingValidator implements FeatureValidator {

    @Override
    public void validatePreProcessing(Options options, Set<Feature> features) {
        if (features.stream().anyMatch(OracleAdvancedQueuing.class::isInstance)) {
            Optional<Feature> first = features.stream().filter(DatabaseDriverFeature.class::isInstance).findFirst();
            if (first.isPresent()) {
                DatabaseDriverFeature databaseDriverFeature = (DatabaseDriverFeature) first.get();
                if (!(databaseDriverFeature instanceof Oracle)) {
                    throw new IllegalArgumentException("Only '" + Oracle.NAME + "' database feature is compatible with Oracle Advanced Queuing");
                }
            }
        }
    }

    @Override
    public void validatePostProcessing(Options options, Set<Feature> features) {
        // no-op
    }
}

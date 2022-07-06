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
package io.micronaut.starter.feature.database;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.validation.FeatureValidator;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.Set;

@Singleton
public class HibernateReactiveJpaFeatureValidator implements FeatureValidator {

    @Override
    public void validatePreProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        if (features.stream().anyMatch(HibernateReactiveJpa.class::isInstance) && features.stream().noneMatch(this::requiredDatabaseFeature)) {
            throw new IllegalArgumentException(
                    HibernateReactiveJpa.NAME + " requires " +
                            MySQL.NAME + ", " +
                            MariaDB.NAME + ", " +
                            PostgreSQL.NAME + ", " +
                            Oracle.NAME + ", or " +
                            SQLServer.NAME
            );
        }
    }

    @Override
    public void validatePostProcessing(Options options, ApplicationType applicationType, Set<Feature> features) {
        // Do nothing
    }

    private boolean requiredDatabaseFeature(Feature f) {
        return f instanceof MySQL
                || f instanceof MariaDB
                || f instanceof PostgreSQL
                || f instanceof Oracle
                || f instanceof SQLServer;
    }
}

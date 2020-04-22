/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.database;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.ConfiguredFeature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.FeatureConfiguration;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;

import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class HibernateJpa extends ConfiguredFeature {

    private final JdbcFeature jdbcFeature;

    public HibernateJpa(@Named("hibernatejpa") FeatureConfiguration featureConfiguration,
                        JdbcFeature jdbcFeature) {
        super(featureConfiguration);
        this.jdbcFeature = jdbcFeature;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(JdbcFeature.class)) {
            featureContext.addFeature(jdbcFeature);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().putAll(ConfigurationHelper.JDBC_H2);
        generatorContext.getConfiguration().putAll(ConfigurationHelper.JPA_DDL);
    }
}

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
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;

import javax.inject.Singleton;

@Singleton
public class DataJpa implements DataFeature {

    private final Data data;
    private final JdbcFeature jdbcFeature;
    private final DatabaseDriverFeature defaultDbFeature;

    public DataJpa(Data data, JdbcFeature jdbcFeature, DatabaseDriverFeature defaultDbFeature) {
        this.data = data;
        this.jdbcFeature = jdbcFeature;
        this.defaultDbFeature = defaultDbFeature;
    }

    @Override
    public String getName() {
        return "data-jpa";
    }

    @Override
    public String getTitle() {
        return "Micronaut Data JPA";
    }

    @Override
    public String getDescription() {
        return "Adds support for Micronaut Data Hibernate/JPA";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeature(data);
        if (!featureContext.isPresent(JdbcFeature.class)) {
            featureContext.addFeature(jdbcFeature);
        }
        if (!featureContext.isPresent(DatabaseDriverFeature.class)) {
            featureContext.addFeature(defaultDbFeature);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().putAll(getDatasourceConfig());
        generatorContext.getConfiguration().putAll(ConfigurationHelper.JPA_DDL);
    }
}

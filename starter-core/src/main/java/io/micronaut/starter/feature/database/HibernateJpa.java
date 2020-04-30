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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;

import javax.inject.Singleton;

@Singleton
public class HibernateJpa implements Feature {

    private final JdbcFeature jdbcFeature;
    private final DatabaseDriverFeature defaultDbFeature;

    public HibernateJpa(JdbcFeature jdbcFeature, DatabaseDriverFeature defaultDbFeature) {
        this.jdbcFeature = jdbcFeature;
        this.defaultDbFeature = defaultDbFeature;
    }

    @Override
    public String getName() {
        return "hibernate-jpa";
    }

    @Override
    public String getTitle() {
        return "Hibernate JPA";
    }

    @Override
    public String getDescription() {
        return "Adds support for Hibernate/JPA";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(JdbcFeature.class)) {
            featureContext.addFeature(jdbcFeature);
        }
        if (!featureContext.isPresent(DatabaseDriverFeature.class)) {
            featureContext.addFeature(defaultDbFeature);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        DatabaseDriverFeature dbFeature = generatorContext.getFeature(DatabaseDriverFeature.class);
        generatorContext.getConfiguration().putAll(ConfigurationHelper.jdbc(dbFeature));
        generatorContext.getConfiguration().putAll(ConfigurationHelper.JPA_DDL);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

}

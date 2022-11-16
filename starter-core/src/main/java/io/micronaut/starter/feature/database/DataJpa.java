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

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;
import io.micronaut.starter.feature.migration.MigrationFeature;
import jakarta.inject.Singleton;

@Singleton
public class DataJpa implements JpaFeature, DataFeature {

    public static final String NAME = "data-jpa";
    private final Data data;
    private final JdbcFeature jdbcFeature;

    public DataJpa(Data data, JdbcFeature jdbcFeature) {
        this.data = data;
        this.jdbcFeature = jdbcFeature;
    }

    @Override
    public String getName() {
        return NAME;
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
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_DATA_PROCESSOR);
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.data")
                .artifactId("micronaut-data-hibernate-jpa")
                .compile());
        DatabaseDriverFeature dbFeature = generatorContext.getRequiredFeature(DatabaseDriverFeature.class);
        generatorContext.getConfiguration().addNested(getDatasourceConfig(dbFeature));
        generatorContext.getConfiguration().put(JPA_HIBERNATE_PROPERTIES_HBM2DDL,
                generatorContext.getFeatures().hasFeature(MigrationFeature.class) ? Hbm2ddlAuto.NONE.toString() :
                        Hbm2ddlAuto.UPDATE.toString());

    }
}

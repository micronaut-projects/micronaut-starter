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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;

import io.micronaut.starter.feature.database.r2dbc.R2dbc;
import io.micronaut.starter.feature.migration.MigrationFeature;
import jakarta.inject.Singleton;

@Singleton
public class Oracle extends DatabaseDriverFeature {

    public static final String NAME = "oracle";

    public Oracle(JdbcFeature jdbcFeature, TestContainers testContainers) {
        super(jdbcFeature, testContainers);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Oracle Database Server";
    }

    @Override
    public String getDescription() {
        return "Adds the Oracle driver and default config";
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:oracle:thin:@localhost:1521/xe";
    }

    @Override
    public String getR2dbcUrl() {
        return "r2dbc:oracle://localhost:1521/xe";
    }

    @Override
    public String getDriverClass() {
        return "oracle.jdbc.OracleDriver";
    }

    @Override
    public String getDefaultUser() {
        return "system";
    }

    @Override
    public String getDefaultPassword() {
        return "oracle";
    }

    @Override
    public String getDataDialect() {
        return "ORACLE";
    }

    @Override
    public boolean embedded() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        if (generatorContext.isFeaturePresent(R2dbc.class)) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId("com.oracle.database.r2dbc")
                    .artifactId("oracle-r2dbc")
                    .runtime());
            if (!generatorContext.isFeaturePresent(MigrationFeature.class)) {
                return;
            }
        }

        if (!generatorContext.isFeaturePresent(DataHibernateReactive.class)) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId("com.oracle.database.jdbc")
                    .artifactId("ojdbc8")
                    .runtime());
        }
    }

}

/*
 * Copyright 2017-2020 original authors
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

import jakarta.inject.Singleton;

@Singleton
public class SQLServer extends DatabaseDriverFeature {

    public SQLServer(JdbcFeature jdbcFeature, TestContainers testContainers) {
        super(jdbcFeature, testContainers);
    }

    @Override
    @NonNull
    public String getName() {
        return "sqlserver";
    }

    @Override
    public String getTitle() {
        return "Microsoft SQL Server";
    }

    @Override
    public String getDescription() {
        return "Adds the SQL Server driver and default config";
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:sqlserver://localhost:1433;databaseName=tempdb";
    }

    @Override
    public String getR2dbcUrl() {
        return "r2dbc:mssql://localhost:1433/tempdb";
    }

    @Override
    public String getDriverClass() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }

    @Override
    public String getDefaultUser() {
        return "sa";
    }

    @Override
    public String getDefaultPassword() {
        return "";
    }

    @Override
    public String getDataDialect() {
        return "SQL_SERVER";
    }

    @Override
    public boolean embedded() {
        return false;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("com.microsoft.sqlserver")
                .artifactId("mssql-jdbc")
                .runtime());
        if (generatorContext.isFeaturePresent(R2dbc.class)) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId("io.r2dbc")
                    .artifactId("r2dbc-mssql")
                    .runtime());
        }
    }
}

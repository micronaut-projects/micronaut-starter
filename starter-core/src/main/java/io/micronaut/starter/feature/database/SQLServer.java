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
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;
import io.micronaut.starter.feature.testresources.DbType;
import io.micronaut.starter.feature.testresources.TestResources;
import jakarta.inject.Singleton;

import java.util.Optional;

import static io.micronaut.starter.feature.database.DataHibernateReactive.IO_VERTX_DEPENDENCY_GROUP;

@Singleton
public class SQLServer extends DatabaseDriverFeature {

    public static final String NAME = "sqlserver";

    public static final String VERTX_MSSQL_CLIENT = "vertx-mssql-client";
    public static final Dependency.Builder DEPENDENCY_MSSQL_JDBC = Dependency.builder()
            .groupId("com.microsoft.sqlserver")
            .artifactId("mssql-jdbc")
            .runtime();

    private static final Dependency.Builder DEPENDENCY_VERTX_MSSQL_CLIENT = Dependency.builder()
            .groupId(IO_VERTX_DEPENDENCY_GROUP)
            .artifactId(VERTX_MSSQL_CLIENT)
            .compile();

    private static final Dependency.Builder DEPENDENCY_MSSQL_R2DBC = Dependency.builder()
            .groupId("io.r2dbc")
            .artifactId("r2dbc-mssql")
            .runtime();

    public SQLServer(JdbcFeature jdbcFeature,
                     TestContainers testContainers,
                     TestResources testResources) {
        super(jdbcFeature, testContainers, testResources);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Microsoft SQL Server";
    }

    @Override
    @NonNull
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

    @NonNull
    @Override
    public Optional<DbType> getDbType() {
        return Optional.of(DbType.SQLSERVER);
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
    @NonNull
    public Optional<Dependency.Builder> getR2DbcDependency() {
        return Optional.of(DEPENDENCY_MSSQL_R2DBC);
    }

    @Override
    @NonNull
    public Optional<Dependency.Builder> getHibernateReactiveJavaClientDependency() {
        return Optional.of(DEPENDENCY_VERTX_MSSQL_CLIENT);
    }

    @Override
    @NonNull
    public Optional<Dependency.Builder> getJavaClientDependency() {
        return Optional.of(DEPENDENCY_MSSQL_JDBC);
    }

    @Override
    public boolean embedded() {
        return false;
    }
}

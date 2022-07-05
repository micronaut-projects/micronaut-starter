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
import jakarta.inject.Singleton;

import java.util.Optional;

import static io.micronaut.starter.feature.database.DataHibernateReactive.IO_VERTX_DEPENDENCY_GROUP;

@Singleton
public class PostgreSQL extends DatabaseDriverFeature {

    public static final String NAME = "postgres";

    public static final String VERTX_PG_CLIENT = "vertx-pg-client";
    private static final Dependency.Builder DEPENDENCY_R2DBC_POSTGRESQL = Dependency.builder()
            .groupId("org.postgresql")
            .artifactId("r2dbc-postgresql")
            .runtime();

    private static final Dependency.Builder DEPENDENCY_POSTGRESQL = Dependency.builder()
            .groupId("org.postgresql")
                    .artifactId("postgresql")
                    .runtime();

    private static final Dependency.Builder DEPENDENCY_VERTX_PG_CLIENT = Dependency.builder()
            .groupId(IO_VERTX_DEPENDENCY_GROUP)
            .artifactId(VERTX_PG_CLIENT)
            .compile();

    public PostgreSQL(JdbcFeature jdbcFeature, TestContainers testContainers) {
        super(jdbcFeature, testContainers);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "PostgresSQL";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds the PostgresSQL driver and default config";
    }

    @Override
    public String getJdbcUrl() {
        // postgres docker image uses default db name and username of postgres so we use the same
        return "jdbc:postgresql://localhost:5432/postgres";
    }

    @Override
    public String getR2dbcUrl() {
        return "r2dbc:postgresql://localhost:5432/postgres";
    }

    @Override
    public String getDriverClass() {
        return "org.postgresql.Driver";
    }

    @Override
    public String getDefaultUser() {
        return NAME;
    }

    @Override
    public String getDefaultPassword() {
        return "";
    }

    @Override
    public String getDataDialect() {
        return "POSTGRES";
    }

    @Override
    public boolean embedded() {
        return false;
    }

    @NonNull
    @Override
    public Optional<Dependency.Builder> getR2DbcDependency() {
        return Optional.of(DEPENDENCY_R2DBC_POSTGRESQL);
    }

    @NonNull
    @Override
    public Optional<Dependency.Builder> getHibernateReactiveJavaClientDependency() {
        return Optional.of(DEPENDENCY_VERTX_PG_CLIENT);
    }

    @NonNull
    @Override
    public Optional<Dependency.Builder> getJavaClientDependency() {
        return Optional.of(DEPENDENCY_POSTGRESQL);
    }
}

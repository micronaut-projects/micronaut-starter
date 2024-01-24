/*
 * Copyright 2017-2024 original authors
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

@Singleton
public class MySQL extends MySQLCompatibleFeature {

    public static final String NAME = "mysql";

    private static final Dependency.Builder DEPENDENCY_R2DBC_MYSQL = Dependency.builder()
            .groupId("dev.miku")
                    .artifactId("r2dbc-mysql")
                    .runtime();

    public static final Dependency.Builder DEPENDENCY_MYSQL_CONNECTOR_JAVA = Dependency.builder()
            .groupId(NAME)
            .artifactId("mysql-connector-java")
            .runtime()
            .template();

    public MySQL(JdbcFeature jdbcFeature,
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
        return "MySQL";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds the MySQL driver and default config";
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:mysql://localhost:3306/db";
    }

    @Override
    public String getR2dbcUrl() {
        return "r2dbc:mysql://localhost:3306/db";
    }

    @Override
    public String getDriverClass() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @NonNull
    @Override
    public Optional<DbType> getDbType() {
        return Optional.of(DbType.MYSQL);
    }

    @Override
    public String getDefaultUser() {
        return "root";
    }

    @Override
    public String getDefaultPassword() {
        return "";
    }

    @Override
    public String getDataDialect() {
        return "MYSQL";
    }

    @Override
    public boolean embedded() {
        return false;
    }

    @Override
    @NonNull
    public Optional<Dependency.Builder> getR2DbcDependency() {
        return Optional.of(DEPENDENCY_R2DBC_MYSQL);
    }

    @Override
    @NonNull
    public Optional<Dependency.Builder> getJavaClientDependency() {
        return Optional.of(DEPENDENCY_MYSQL_CONNECTOR_JAVA);
    }
}

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
public class Oracle extends DatabaseDriverFeature {

    public static final String NAME = "oracle";
    public static final String VERTX_ORACLE_CLIENT = "vertx-oracle-client";

    //TODO: enable once ojdbc upgrades to 23. See: https://github.com/micronaut-projects/micronaut-sql/pull/1268
    public static final boolean COMPATIBLE_WITH_HIBERNATE_REACTIVE = false;

    @Deprecated(forRemoval = true)
    public static final Dependency.Builder DEPENDENCY_OJDBC8 = Dependency.builder()
            .groupId("com.oracle.database.jdbc")
            .artifactId("ojdbc8")
            .runtime()
            .template();

    public static final Dependency.Builder DEPENDENCY_OJDBC11 = Dependency.builder()
            .groupId("com.oracle.database.jdbc")
            .artifactId("ojdbc11")
            .runtime()
            .template();

    private static final Dependency.Builder DEPENDENCY_ORACLE_R2DBC = Dependency.builder()
            .groupId("com.oracle.database.r2dbc")
            .artifactId("oracle-r2dbc")
            .runtime();

    private static final Dependency.Builder DEPENDENCY_VERTX_ORACLE_CLIENT = Dependency.builder()
            .groupId(IO_VERTX_DEPENDENCY_GROUP)
            .artifactId(VERTX_ORACLE_CLIENT)
            .compile();

    public Oracle(JdbcFeature jdbcFeature,
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
        return "Oracle Database Server";
    }

    @Override
    @NonNull
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

    @NonNull
    @Override
    public Optional<DbType> getDbType() {
        return Optional.of(DbType.ORACLEFREE);
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
    @NonNull
    public Optional<Dependency.Builder> getR2DbcDependency() {
        return Optional.of(DEPENDENCY_ORACLE_R2DBC);
    }

    @Override
    @NonNull
    public Optional<Dependency.Builder> getHibernateReactiveJavaClientDependency() {
        return COMPATIBLE_WITH_HIBERNATE_REACTIVE ? Optional.of(DEPENDENCY_VERTX_ORACLE_CLIENT) : Optional.empty();
    }

    @Override
    @NonNull
    public Optional<Dependency.Builder> getJavaClientDependency() {
        return Optional.of(DEPENDENCY_OJDBC11);
    }

    @Override
    public boolean embedded() {
        return false;
    }
}

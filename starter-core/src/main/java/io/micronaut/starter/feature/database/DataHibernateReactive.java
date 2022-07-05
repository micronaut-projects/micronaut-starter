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
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.build.dependencies.Priority;
import io.micronaut.starter.feature.FeatureContext;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class DataHibernateReactive extends TestContainersFeature implements JpaFeature, DataFeature {

    public static final String NAME = "data-hibernate-reactive";

    public static final String JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_URL = "jpa.default.properties.hibernate.connection.url";
    public static final String JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_USERNAME = "jpa.default.properties.hibernate.connection.username";
    public static final String JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_PASSWORD = "jpa.default.properties.hibernate.connection.password";
    public static final String JPA_DEFAULT_REACTIVE = "jpa.default.reactive";

    public static final String IO_VERTX_DEPENDENCY_GROUP = "io.vertx";
    public static final String VERTX_MYSQL_CLIENT = "vertx-mysql-client";
    public static final String VERTX_PG_CLIENT = "vertx-pg-client";
    public static final String VERTX_MSSQL_CLIENT = "vertx-mssql-client";
    public static final String VERTX_ORACLE_CLIENT = "vertx-oracle-client";

    private static final Dependency.Builder DEPENDENCY_VERTX_MYSQL_CLIENT = Dependency.builder()
            .groupId(IO_VERTX_DEPENDENCY_GROUP)
            .artifactId(VERTX_MYSQL_CLIENT)
            .compile();

    private static final Dependency.Builder DEPENDENCY_VERTX_PG_CLIENT = Dependency.builder()
            .groupId(IO_VERTX_DEPENDENCY_GROUP)
                    .artifactId(VERTX_PG_CLIENT)
                    .compile();

    private static final Dependency.Builder DEPENDENCY_VERTX_MSSQL_CLIENT = Dependency.builder()
            .groupId(IO_VERTX_DEPENDENCY_GROUP)
                    .artifactId(VERTX_MSSQL_CLIENT)
                    .compile();

    private static final Dependency.Builder DEPENDENCY_VERTX_ORACLE_CLIENT = Dependency.builder()
            .groupId(IO_VERTX_DEPENDENCY_GROUP)
                    .artifactId(VERTX_ORACLE_CLIENT)
                    .compile();

    private static final Dependency.Builder DEPENDENCY_MICRONAUT_DATA_HIBERNATE_REACTIVE = MicronautDependencyUtils.dataDependency()
            .artifactId("micronaut-data-hibernate-reactive")
                .compile();

    private static final Dependency.Builder DEPENDENCY_MICRONAUT_DATA_PROCESSOR = MicronautDependencyUtils.dataDependency()
            .artifactId("micronaut-data-processor")
            .versionProperty("micronaut.data.version")
            .order(Priority.MICRONAUT_DATA_PROCESSOR.getOrder())
            .annotationProcessor(true);

    private final Data data;

    public DataHibernateReactive(Data data, TestContainers testContainers) {
        super(testContainers);
        this.data = data;
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut Data Hibernate Reactive";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds support for Micronaut Data Hibernate Reactive";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeature(data);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_DATA_PROCESSOR);
        generatorContext.addDependency(DEPENDENCY_MICRONAUT_DATA_HIBERNATE_REACTIVE);
        DatabaseDriverFeature dbFeature = generatorContext.getRequiredFeature(DatabaseDriverFeature.class);
        generatorContext.getConfiguration().put("jpa.default.properties.hibernate.hbm2ddl.auto", "update");

        parseDependency(dbFeature).ifPresent(dependencyBuilder -> {
            generatorContext.addDependency(dependencyBuilder);
            generatorContext.getConfiguration().put(JPA_DEFAULT_REACTIVE, true);
            generatorContext.getConfiguration().put(JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_URL, dbFeature.getJdbcUrl());
            generatorContext.getConfiguration().put(JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_USERNAME, dbFeature.getDefaultUser());
            generatorContext.getConfiguration().put(JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_PASSWORD, dbFeature.getDefaultPassword());
        });
    }

    @NonNull
    private Optional<Dependency.Builder> parseDependency(@NonNull DatabaseDriverFeature dbFeature) {
        if (dbFeature instanceof MySQL || dbFeature instanceof MariaDB) {
            return Optional.of(DEPENDENCY_VERTX_MYSQL_CLIENT);
        } else if (dbFeature instanceof PostgreSQL) {
            return Optional.of(DEPENDENCY_VERTX_PG_CLIENT);
        } else if (dbFeature instanceof SQLServer) {
            return Optional.of(DEPENDENCY_VERTX_MSSQL_CLIENT);
        } else if (dbFeature instanceof Oracle) {
            return Optional.of(DEPENDENCY_VERTX_ORACLE_CLIENT);
        }
        return Optional.empty();
    }

    public String getUrlKey() {
        return JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_URL;
    }
}

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
import io.micronaut.starter.build.dependencies.Priority;
import io.micronaut.starter.feature.FeatureContext;
import jakarta.inject.Singleton;

@Singleton
public class DataHibernateReactive implements JpaFeature, DataFeature {

    public static final String IO_VERTX_DEPENDENCY_GROUP = "io.vertx";
    public static final String NAME = "data-hibernate-reactive";
    private final Data data;

    public DataHibernateReactive(Data data) {
        this.data = data;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut Data Hibernate Reactive";
    }

    @Override
    public String getDescription() {
        return "Adds support for Micronaut Data Hibernate Reactive";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeature(data);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.data")
                .artifactId("micronaut-data-processor")
                .versionProperty("micronaut.data.version")
                .order(Priority.MICRONAUT_DATA_PROCESSOR.getOrder())
                .annotationProcessor(true));

        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.data")
                .artifactId("micronaut-data-hibernate-reactive")
                .compile());

        DatabaseDriverFeature dbFeature = generatorContext.getRequiredFeature(DatabaseDriverFeature.class);
        generatorContext.getConfiguration().put("jpa.default.properties.hibernate.hbm2ddl.auto", "update");

        if (getDatasourceClient(generatorContext, dbFeature)) {
            generatorContext.getConfiguration().put("jpa.default.reactive", true);
            generatorContext.getConfiguration().put("jpa.default.properties.hibernate.connection.url", dbFeature.getJdbcUrl());
            generatorContext.getConfiguration().put("jpa.default.properties.hibernate.connection.username", dbFeature.getDefaultUser());
            generatorContext.getConfiguration().put("jpa.default.properties.hibernate.connection.password", dbFeature.getDefaultPassword());
        }
    }

    private boolean getDatasourceClient(GeneratorContext ctx, DatabaseDriverFeature dbFeature) {
        if (dbFeature instanceof MySQL || dbFeature instanceof MariaDB) {
            ctx.addDependency(Dependency.builder()
                    .groupId(IO_VERTX_DEPENDENCY_GROUP)
                    .artifactId("vertx-mysql-client")
                    .compile());
            return true;
        } else if (dbFeature instanceof PostgreSQL) {
            ctx.addDependency(Dependency.builder()
                    .groupId(IO_VERTX_DEPENDENCY_GROUP)
                    .artifactId("vertx-pg-client")
                    .compile());
            return true;
        } else if (dbFeature instanceof SQLServer) {
            ctx.addDependency(Dependency.builder()
                    .groupId(IO_VERTX_DEPENDENCY_GROUP)
                    .artifactId("vertx-mssql-client")
                    .compile());
            return true;
        } else if (dbFeature instanceof Oracle) {
            ctx.addDependency(Dependency.builder()
                    .groupId(IO_VERTX_DEPENDENCY_GROUP)
                    .artifactId("vertx-oracle-client")
                    .compile());
            return true;
        }
        // Not found, stop configuring properties
        return false;
    }
}

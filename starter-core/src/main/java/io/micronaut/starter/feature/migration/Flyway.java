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
package io.micronaut.starter.feature.migration;

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.database.MariaDB;
import io.micronaut.starter.feature.database.MySQL;
import io.micronaut.starter.feature.database.Oracle;
import io.micronaut.starter.feature.database.PostgreSQL;
import io.micronaut.starter.feature.database.SQLServer;
import io.micronaut.starter.feature.oraclecloud.OracleCloudAutonomousDatabase;
import jakarta.inject.Singleton;

@Singleton
public class Flyway implements MigrationFeature {

    public static final String NAME = "flyway";
    public static final String ARTIFACT_ID_MICRONAUT_FLYWAY = "micronaut-flyway";
    public static final String GROUP_ID_FLYWAYDB = "org.flywaydb";

    public static final String ARTIFACT_ID_FLYWAY_MYSQL = "flyway-mysql";
    public static final String ARTIFACT_ID_FLYWAY_SQLSERVER = "flyway-sqlserver";
    public static final String ARTIFACT_ID_FLYWAY_ORACLE = "flyway-database-oracle";
    //https://documentation.red-gate.com/fd/oracle-184127602.html
    public static final String ARTIFACT_ID_FLYWAY_POSTGRESQL = "flyway-database-postgresql";
    public static final Dependency.Builder DEPENDENCY_FLYWAY_MYSQL = Dependency.builder()
            .groupId(GROUP_ID_FLYWAYDB)
            .artifactId(ARTIFACT_ID_FLYWAY_MYSQL)
            .runtime();

    public static final Dependency.Builder DEPENDENCY_FLYWAY_ORACLE = Dependency.builder()
            .groupId(GROUP_ID_FLYWAYDB)
            .artifactId(ARTIFACT_ID_FLYWAY_ORACLE)
            .runtime();
    public static final Dependency.Builder DEPENDENCY_FLYWAY_POSTGRESQL = Dependency.builder()
            .groupId(GROUP_ID_FLYWAYDB)
            .artifactId(ARTIFACT_ID_FLYWAY_POSTGRESQL)
            .runtime();
    public static final Dependency.Builder DEPENDENCY_FLYWAY_SQLSERVER = Dependency.builder()
            .groupId(GROUP_ID_FLYWAYDB)
            .artifactId(ARTIFACT_ID_FLYWAY_SQLSERVER)
            .runtime();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Flyway Database Migration";
    }

    @Override
    public String getDescription() {
        return "Adds support for Flyway database migrations";
    }

    @Override
    public String getThirdPartyDocumentation() {
        return "https://flywaydb.org/";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-flyway/latest/guide/index.html";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        addDependencies(generatorContext);
        generatorContext.getConfiguration().addNested("flyway.datasources.default.enabled", true);
    }

    protected void addDependencies(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.flywayDependency()
                .artifactId(ARTIFACT_ID_MICRONAUT_FLYWAY)
                .compile());
        if (generatorContext.isFeaturePresent(MySQL.class) || generatorContext.isFeaturePresent(MariaDB.class)) {
            generatorContext.addDependency(DEPENDENCY_FLYWAY_MYSQL);
        }
        if (generatorContext.isFeaturePresent(SQLServer.class)) {
            generatorContext.addDependency(DEPENDENCY_FLYWAY_SQLSERVER);
        }
        if (generatorContext.isFeaturePresent(Oracle.class) || generatorContext.isFeaturePresent(OracleCloudAutonomousDatabase.class)) {
            generatorContext.addDependency(DEPENDENCY_FLYWAY_ORACLE);
        }
        if (generatorContext.isFeaturePresent(PostgreSQL.class)) {
            generatorContext.addDependency(DEPENDENCY_FLYWAY_POSTGRESQL);
        }
    }
}


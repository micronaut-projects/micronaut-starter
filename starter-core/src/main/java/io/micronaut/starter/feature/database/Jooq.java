/*
 * Copyright 2017-2023 original authors
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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.gradle.GradlePlugin;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;
import io.micronaut.starter.feature.database.templates.jooqGradleConfiguration;
import io.micronaut.starter.template.RockerWritable;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class Jooq implements Feature {

    private final JdbcFeature jdbcFeature;

    public Jooq(JdbcFeature jdbcFeature) {
        this.jdbcFeature = jdbcFeature;
    }

    @Override
    public String getName() {
        return "jooq";
    }

    @Override
    public String getTitle() {
        return "jOOQ";
    }

    @Override
    public String getDescription() {
        return "Use the jOOQ fluent API for typesafe SQL query construction and execution";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(JdbcFeature.class)) {
            featureContext.addFeature(jdbcFeature);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.sql")
                .artifactId("micronaut-jooq")
                .compile());
        Optional<DatabaseDriverFeature> dbDriverOptional = generatorContext.getFeature(DatabaseDriverFeature.class);
        if (generatorContext.getBuildTool().isGradle() && dbDriverOptional.isPresent()) {
            DatabaseDriverFeature dbDriver = dbDriverOptional.get();
            generatorContext.addHelpLink("jOOQ Generation with the Gradle plugin", "https://www.jooq.org/doc/3.19/manual/code-generation/codegen-gradle/");
            generatorContext.addHelpLink("jOOQ Configuration Reference", "https://www.jooq.org/doc/3.19/manual/code-generation/codegen-configuration/");
            generatorContext.addBuildPlugin(GradlePlugin.builder()
                    .id("org.jooq.jooq-codegen-gradle")
                    .lookupArtifactId("org.jooq.jooq-codegen-gradle.gradle.plugin")
                    .extension(new RockerWritable(jooqGradleConfiguration.template(dbDriver, jooqGeneratorDb(dbDriver))))
                    .build());
        }
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jooq";
    }

    // see https://www.jooq.org/doc/3.19/manual/code-generation/codegen-configuration/
    private String jooqGeneratorDb(DatabaseDriverFeature dbDriver) {
        return switch (dbDriver.getName()) {
            case "h2" -> "org.jooq.meta.h2.H2Database";
            case "mariadb" -> "org.jooq.meta.mariadb.MariaDBDatabase";
            case "mysql" -> "org.jooq.meta.mysql.MySQLDatabase";
            case "oracle" -> "org.jooq.meta.oracle.OracleDatabase";
            case "postgres" -> "org.jooq.meta.postgres.PostgresDatabase";
            case "sqlserver" -> "org.jooq.meta.sqlserver.SQLServerDatabase";
            default -> "";
        };
    }
}

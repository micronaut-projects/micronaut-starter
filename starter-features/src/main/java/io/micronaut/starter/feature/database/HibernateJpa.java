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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;

import io.micronaut.starter.feature.migration.MigrationFeature;
import jakarta.inject.Singleton;

@Singleton
public class HibernateJpa implements JpaFeature {

    private final JdbcFeature jdbcFeature;

    public HibernateJpa(JdbcFeature jdbcFeature) {
        this.jdbcFeature = jdbcFeature;
    }

    @Override
    public String getName() {
        return "hibernate-jpa";
    }

    @Override
    public String getTitle() {
        return "Hibernate JPA";
    }

    @Override
    public String getDescription() {
        return "Adds support for Hibernate/JPA";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(JdbcFeature.class)) {
            featureContext.addFeature(jdbcFeature);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().put(JPA_HIBERNATE_PROPERTIES_HBM2DDL,
                generatorContext.getFeatures().hasFeature(MigrationFeature.class) ? Hbm2ddlAuto.NONE.toString() :
                        Hbm2ddlAuto.UPDATE.toString());
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.sql")
                .artifactId("micronaut-hibernate-jpa")
                .compile());
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
        return "https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#hibernate";
    }
}

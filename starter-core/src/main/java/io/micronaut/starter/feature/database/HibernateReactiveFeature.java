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
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.migration.MigrationFeature;
import io.micronaut.starter.feature.testresources.EaseTestingFeature;
import io.micronaut.starter.feature.testresources.TestResources;

import java.util.Optional;

public abstract class HibernateReactiveFeature extends EaseTestingFeature implements JpaFeature {

    public static final String JPA_DEFAULT_REACTIVE = "jpa.default.reactive";

    public static final String IO_VERTX_DEPENDENCY_GROUP = "io.vertx";

    HibernateReactiveFeature(TestContainers testContainers, TestResources testResources) {
        super(testContainers, testResources);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        DatabaseDriverFeature dbFeature = generatorContext.getRequiredFeature(DatabaseDriverFeature.class);
        generatorContext.getConfiguration().put(JPA_HIBERNATE_PROPERTIES_HBM2DDL,
                generatorContext.getFeatures().hasFeature(MigrationFeature.class) ? Hbm2ddlAuto.NONE.toString() :
                        Hbm2ddlAuto.UPDATE.toString());

        generatorContext.getConfiguration().put(JPA_DEFAULT_REACTIVE, true);
        if (!generatorContext.isFeaturePresent(TestResources.class)) {
            Optional<MigrationFeature> migrationFeature = generatorContext.getFeatures().getFeature(MigrationFeature.class);
            generatorContext.getConfiguration().put(JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_URL,
                    migrationFeature.map(f -> "${datasources.default.url}").orElse(dbFeature.getJdbcUrl()));
            generatorContext.getConfiguration().put(JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_USERNAME,
                    migrationFeature.map(f -> "${datasources.default.username}").orElse(dbFeature.getDefaultUser()));
            generatorContext.getConfiguration().put(JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_PASSWORD,
                    migrationFeature.map(f -> "${datasources.default.password}").orElse(dbFeature.getDefaultPassword()));
        } else {
            dbFeature.getDbType().ifPresent(type ->
                    generatorContext.getConfiguration().put(JPA_HIBERNATE_PROPERTIES_CONNECTION + ".db-type", type.toString())
            );
        }
    }

    public String getUrlKey() {
        return JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_URL;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }
}

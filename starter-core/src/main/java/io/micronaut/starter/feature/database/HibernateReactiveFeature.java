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

public abstract class HibernateReactiveFeature extends TestContainersFeature implements JpaFeature {

    public static final String JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_URL = "jpa.default.properties.hibernate.connection.url";
    public static final String JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_USERNAME = "jpa.default.properties.hibernate.connection.username";
    public static final String JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_PASSWORD = "jpa.default.properties.hibernate.connection.password";
    public static final String JPA_DEFAULT_REACTIVE = "jpa.default.reactive";

    public static final String IO_VERTX_DEPENDENCY_GROUP = "io.vertx";

    HibernateReactiveFeature(TestContainers testContainers) {
        super(testContainers);
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        DatabaseDriverFeature dbFeature = generatorContext.getRequiredFeature(DatabaseDriverFeature.class);
        generatorContext.getConfiguration().put("jpa.default.properties.hibernate.hbm2ddl.auto", "update");

        generatorContext.getConfiguration().put(JPA_DEFAULT_REACTIVE, true);
        generatorContext.getConfiguration().put(JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_URL, dbFeature.getJdbcUrl());
        generatorContext.getConfiguration().put(JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_USERNAME, dbFeature.getDefaultUser());
        generatorContext.getConfiguration().put(JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_PASSWORD, dbFeature.getDefaultPassword());
    }

    public String getUrlKey() {
        return JPA_DEFAULT_PROPERTIES_HIBERNATE_CONNECTION_URL;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }
}

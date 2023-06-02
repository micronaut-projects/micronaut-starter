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
import io.micronaut.starter.feature.testresources.TestResources;

import java.util.Optional;

import static io.micronaut.starter.feature.database.DataHibernateReactive.IO_VERTX_DEPENDENCY_GROUP;

/**
 * Marker interface for classes compatible with MySQL such as {@link MySQL} or {@link MariaDB}.
 */
public abstract class MySQLCompatibleFeature extends DatabaseDriverFeature {

    public static final String VERTX_MYSQL_CLIENT = "vertx-mysql-client";
    private static final Dependency.Builder DEPENDENCY_VERTX_MYSQL_CLIENT = Dependency.builder()
            .groupId(IO_VERTX_DEPENDENCY_GROUP)
            .artifactId(VERTX_MYSQL_CLIENT)
            .compile();

    public MySQLCompatibleFeature(JdbcFeature jdbcFeature, TestContainers testContainers, TestResources testResources) {
        super(jdbcFeature, testContainers, testResources);
    }

    @Override
    @NonNull
    public Optional<Dependency.Builder> getHibernateReactiveJavaClientDependency() {
        return Optional.of(DEPENDENCY_VERTX_MYSQL_CLIENT);
    }
}

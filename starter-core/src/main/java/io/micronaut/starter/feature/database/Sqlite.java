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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.util.StringUtils;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;
import io.micronaut.starter.feature.testresources.TestResources;
import jakarta.inject.Singleton;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@Requires(property = "micronaut.starter.feature.jdbc.sqlite.enabled", value = StringUtils.TRUE, defaultValue = StringUtils.TRUE)
@Singleton
public class Sqlite extends DatabaseDriverFeature {
    public static final String NAME = "sqlite";
    public static final String MICRONAUT_SQLITE_ARTIFACT = "micronaut-jdbc-sqlite";

    public Sqlite(JdbcFeature jdbcFeature, TestContainers testContainers, TestResources testResources) {
        super(jdbcFeature, testContainers, testResources);
    }

    @Override
    public boolean embedded() {
        return true;
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:sqlite:file:%s?mode=memory&cache=shared&foreign_keys=on&busy_timeout=5000";
    }

    @Override
    public String getR2dbcUrl() {
        return null;
    }

    @Override
    public String getDriverClass() {
        return "org.sqlite.JDBC";
    }

    @Override
    public String getDefaultUser() {
        return null;
    }

    @Override
    public String getDefaultPassword() {
        return null;
    }

    @Override
    public String getDataDialect() {
        return "SQLITE";
    }

    @Override
    public @NonNull String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "SQLite";
    }

    @Override
    public String getDescription() {
        return "It adds the Micronaut SQLite dependency";
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-sql/latest/guide/#jdbc-sqlite";
    }

    @Override
    public @Nullable String getThirdPartyDocumentation() {
        return "https://www.sqlite.org/";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MicronautDependencyUtils.sqlDependency()
                .artifactId(MICRONAUT_SQLITE_ARTIFACT)
                .compile());
    }
}

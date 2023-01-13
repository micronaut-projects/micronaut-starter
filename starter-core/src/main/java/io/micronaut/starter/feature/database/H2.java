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

import io.micronaut.context.annotation.Primary;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.build.dependencies.Dependency;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
@Primary
public class H2 extends DatabaseDriverFeature {

    public static final String NAME = "h2";

    private static final Dependency.Builder DEPENDENCY_H2 = Dependency.builder()
            .groupId("com.h2database")
            .artifactId("h2")
            .runtime();

    private static final Dependency.Builder DEPENDENCY_R2DBC_H2 = Dependency.builder()
            .groupId("io.r2dbc")
            .artifactId("r2dbc-h2")
            .runtime();

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "H2 Database";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "Adds the H2 driver and default config";
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:h2:mem:devDb;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE";
    }

    @Override
    public String getR2dbcUrl() {
        return "r2dbc:h2:mem:///testdb;DB_CLOSE_ON_EXIT=FALSE";
    }

    @Override
    public String getDriverClass() {
        return "org.h2.Driver";
    }

    @Override
    public String getDefaultUser() {
        return "sa";
    }

    @Override
    public String getDefaultPassword() {
        return "";
    }

    @Override
    public String getDataDialect() {
        return "H2";
    }

    @Override
    public boolean embedded() {
        return true;
    }

    @Override
    @NonNull
    public Optional<Dependency.Builder> getR2DbcDependency() {
        return Optional.of(DEPENDENCY_R2DBC_H2);
    }

    @Override
    @NonNull
    public Optional<Dependency.Builder> getJavaClientDependency() {
        return Optional.of(DEPENDENCY_H2);
    }
}

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
import io.micronaut.starter.feature.database.MariaDB;
import io.micronaut.starter.feature.database.MySQL;
import io.micronaut.starter.feature.database.SQLServer;
import jakarta.inject.Singleton;

import java.util.LinkedHashMap;
import java.util.Map;

@Singleton
public class Flyway implements MigrationFeature {

    public static final String NAME = "flyway";

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
        generatorContext.addDependency(Dependency.builder()
                .groupId("io.micronaut.flyway")
                .artifactId("micronaut-flyway")
                .compile());
        if (generatorContext.isFeaturePresent(MySQL.class) || generatorContext.isFeaturePresent(MariaDB.class)) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId("org.flywaydb")
                    .artifactId("flyway-mysql")
                    .runtime());
        }
        if (generatorContext.isFeaturePresent(SQLServer.class)) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId("org.flywaydb")
                    .artifactId("flyway-sqlserver")
                    .runtime());
        }
    }

    @Override
    public Map<String, Object> getAdditionalConfig() {
        Map<String, Object> config = new LinkedHashMap<>(2);
        config.put("flyway.datasources.default.enabled", true);
        return config;
    }
}


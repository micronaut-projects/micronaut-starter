/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.feature.database;

import io.micronaut.starter.application.generator.GeneratorContext;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.inject.Singleton;

@Singleton
public class PostgreSQL implements DatabaseDriverFeature {

    private static final Map<String, String> JDBC_CONFIG;

    static {
        // postgres docker image uses default db name and username of postgres so we use the same
        String prefix = "datasources.default.";
        JDBC_CONFIG = new LinkedHashMap<>();
        JDBC_CONFIG.put(prefix + "url", "jdbc:postgresql://localhost:5432/postgres");
        JDBC_CONFIG.put(prefix + "driverClassName", "org.postgresql.Driver");
        JDBC_CONFIG.put(prefix + "username", "postgres");
        JDBC_CONFIG.put(prefix + "password", "");
    }

    @Override
    public String getName() {
        return "postgres";
    }

    @Override
    public String getTitle() {
        return "PostgresSQL open source object-relational database system.";
    }

    @Override
    public String getDescription() {
        return "Adds the PostgresSQL driver and default config.";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getConfiguration().putAll(JDBC_CONFIG);
    }

}

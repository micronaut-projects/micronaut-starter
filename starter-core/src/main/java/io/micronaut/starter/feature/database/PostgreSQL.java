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

import javax.inject.Singleton;

@Singleton
public class PostgreSQL implements DatabaseDriverFeature {

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
    public String getJdbcUrl() {
        // postgres docker image uses default db name and username of postgres so we use the same
        return "jdbc:postgresql://localhost:5432/postgres";
    }

    @Override
    public String getDriverClass() {
        return "org.postgresql.Driver";
    }

    @Override
    public String getDefaultUser() {
        return "postgres";
    }

    @Override
    public String getDefaultPassword() {
        return "";
    }
}

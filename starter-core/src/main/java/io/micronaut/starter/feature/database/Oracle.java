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

import edu.umd.cs.findbugs.annotations.NonNull;

import javax.inject.Singleton;

@Singleton
public class Oracle extends DatabaseDriverFeature {

    public Oracle(TestContainers testContainers) {
        super(testContainers);
    }

    @Override
    @NonNull
    public String getName() {
        return "oracle";
    }

    @Override
    public String getTitle() {
        return "Oracle Database Server.";
    }

    @Override
    public String getDescription() {
        return "Adds the Oracle driver and default config.";
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:oracle:thin:@localhost:1521/xe";
    }

    @Override
    public String getR2dbcUrl() {
        throw new UnsupportedOperationException("R2DBC is not yet supported by Oracle");
    }

    @Override
    public String getDriverClass() {
        return "oracle.jdbc.OracleDriver";
    }

    @Override
    public String getDefaultUser() {
        return "system";
    }

    @Override
    public String getDefaultPassword() {
        return "oracle";
    }

    @Override
    public String getDataDialect() {
        return "ORACLE";
    }

    @Override
    public boolean embedded() {
        return false;
    }
}

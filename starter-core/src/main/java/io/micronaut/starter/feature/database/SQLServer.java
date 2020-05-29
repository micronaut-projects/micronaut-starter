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
public class SQLServer implements DatabaseDriverFeature {

    @Override
    @NonNull
    public String getName() {
        return "sqlserver";
    }

    @Override
    public String getTitle() {
        return "Microsoft SQL Server.";
    }

    @Override
    public String getDescription() {
        return "Adds the SQL Server driver and default config.";
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:sqlserver://localhost:1433;databaseName=tempdb";
    }

    @Override
    public String getDriverClass() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
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
        return "SQL_SERVER";
    }
}

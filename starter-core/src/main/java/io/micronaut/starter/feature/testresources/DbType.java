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
package io.micronaut.starter.feature.testresources;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.testresources.buildtools.KnownModules;

public enum DbType {

    MARIADB("mariadb", KnownModules.JDBC_MARIADB, "JDBC_MARIADB", KnownModules.R2DBC_MARIADB, "R2DBC_MARIADB"),
    MYSQL("mysql", KnownModules.JDBC_MYSQL, "JDBC_MYSQL", KnownModules.R2DBC_MYSQL, "R2DBC_MYSQL"),
    POSTGRESQL("postgresql", KnownModules.JDBC_POSTGRESQL, "JDBC_POSTGRESQL", KnownModules.R2DBC_POSTGRESQL, "R2DBC_POSTGRESQL"),
    SQLSERVER("mssql", KnownModules.JDBC_MSSQL, "JDBC_MSSQL", KnownModules.R2DBC_MSSQL, "R2DBC_MSSQL"),
    ORACLEXE("oracle", KnownModules.JDBC_ORACLE_XE, "JDBC_ORACLE_XE", KnownModules.R2DBC_ORACLE_XE, "R2DBC_ORACLE_XE");

    private final String name;
    private final String jdbcTestResourcesModuleName;
    private final String gradleJdbcTestResourceModuleName;
    private final String r2dbcTestResourcesModuleName;
    private final String gradleR2dbcTestResourcesModuleName;

    DbType(String name, String jdbcTestResourcesModuleName, String gradleJdbcTestResourceModuleName, String r2dbcTestResourcesModuleName, String gradleR2dbcTestResourcesModuleName) {
        this.name = name;
        this.jdbcTestResourcesModuleName = jdbcTestResourcesModuleName;
        this.gradleJdbcTestResourceModuleName = gradleJdbcTestResourceModuleName;
        this.r2dbcTestResourcesModuleName = r2dbcTestResourcesModuleName;
        this.gradleR2dbcTestResourcesModuleName = gradleR2dbcTestResourcesModuleName;
    }

    @NonNull
    public String getJdbcTestResourcesModuleName() {
        return jdbcTestResourcesModuleName;
    }

    @NonNull
    public String getGradleJdbcTestResourceModuleName() {
        return gradleJdbcTestResourceModuleName;
    }

    @NonNull
    public String getR2dbcTestResourcesModuleName() {
        return r2dbcTestResourcesModuleName;
    }

    @NonNull
    public String getGradleR2dbcTestResourcesModuleName() {
        return gradleR2dbcTestResourcesModuleName;
    }

    @Override
    public String toString() {
        return name;
    }
}

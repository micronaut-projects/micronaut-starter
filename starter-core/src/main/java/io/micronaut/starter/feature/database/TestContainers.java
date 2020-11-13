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
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.database.r2dbc.R2dbcFeature;
import io.micronaut.starter.template.PropertiesTemplate;
import io.micronaut.starter.template.StringTemplate;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.Map;

@Singleton
public class TestContainers implements Feature {

    @NonNull
    @Override
    public String getName() {
        return "testcontainers";
    }

    @Override
    public String getTitle() {
        return "Testcontainers";
    }

    @Override
    public String getDescription() {
        return "Testcontainers adds support for running real databases in a docker container for the test environment";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getFeature(DatabaseDriverFeature.class).ifPresent(driverFeature -> {
            generatorContext.getFeature(R2dbcFeature.class).ifPresent(driverConfiguration -> {
                String url = null;
                if (driverFeature instanceof MySQL) {
                    url = "r2dbc:tc:mysql:///db?TC_IMAGE_TAG=8";
                } else if (driverFeature instanceof PostgreSQL) {
                    url = "r2dbc:tc:postgresql:///db?TC_IMAGE_TAG=12";
                } else if (driverFeature instanceof MariaDB) {
                    url = "r2dbc:tc:mariadb:///db?TC_IMAGE_TAG=10";
                } else if (driverFeature instanceof SQLServer) {
                    url = "r2dbc:tc:sqlserver:///db?TC_IMAGE_TAG=2019-CU4-ubuntu-16.04";
                    generatorContext.addTemplate("sqlserverEula", new StringTemplate("src/test/resources/container-license-acceptance.txt", "mcr.microsoft.com/mssql/server:2019-CU4-ubuntu-16.04"));
                }

                if (url != null) {
                    Map<String, Object> testConfig = generatorContext.getEnvConfiguration("test");
                    testConfig.put(driverConfiguration.getUrlKey(), url);
                }
            });
            generatorContext.getFeature(DatabaseDriverConfigurationFeature.class).ifPresent(driverConfiguration -> {
                String url = null;
                String driver = "org.testcontainers.jdbc.ContainerDatabaseDriver";
                if (driverFeature instanceof MySQL) {
                    url = "jdbc:tc:mysql:8:///db";
                } else if (driverFeature instanceof PostgreSQL) {
                    url = "jdbc:tc:postgresql:12:///postgres";
                } else if (driverFeature instanceof MariaDB) {
                    url = "jdbc:tc:mariadb:10:///db";
                } else if (driverFeature instanceof SQLServer) {
                    url = "jdbc:tc:sqlserver:2019-CU4-ubuntu-16.04://databaseName=tempdb";
                    generatorContext.addTemplate("sqlserverEula", new StringTemplate("src/test/resources/container-license-acceptance.txt", "mcr.microsoft.com/mssql/server:2019-CU4-ubuntu-16.04"));
                } else if (driverFeature instanceof Oracle) {
                    url = "jdbc:tc:oracle:thin:@/xe";
                    generatorContext.addTemplate("testContainersProperties", new PropertiesTemplate("src/test/resources/testcontainers.properties", Collections.singletonMap("oracle.container.image", "wnameless/oracle-xe-11g-r2")));
                }

                if (url != null) {
                    Map<String, Object> testConfig = generatorContext.getEnvConfiguration("test");
                    testConfig.put(driverConfiguration.getUrlKey(), url);
                    testConfig.put(driverConfiguration.getDriverKey(), driver);
                }
            });
        });
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://www.testcontainers.org/";
    }
}

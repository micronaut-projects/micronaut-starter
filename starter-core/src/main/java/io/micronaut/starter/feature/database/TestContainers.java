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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.config.ApplicationConfiguration;
import io.micronaut.starter.feature.config.Configuration;
import io.micronaut.starter.feature.database.r2dbc.R2dbc;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.PropertiesTemplate;
import io.micronaut.starter.template.StringTemplate;

import javax.inject.Singleton;
import java.util.Collections;

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
        generatorContext.addDependency(Dependency.builder()
                .groupId("org.testcontainers")
                .artifactId("testcontainers")
                .test());
        generatorContext.getFeature(DatabaseDriverFeature.class).ifPresent(driverFeature -> {
            generatorContext.getFeature(R2dbc.class).ifPresent(driverConfiguration -> {
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
                    Configuration testConfig = generatorContext.getConfiguration("test", ApplicationConfiguration.testConfig());
                    testConfig.put(driverConfiguration.getUrlKey(), url);
                }
                generatorContext.addDependency(Dependency.builder()
                        .groupId("org.testcontainers")
                        .artifactId("r2dbc")
                        .test());
            });
            generatorContext.getFeature(DatabaseDriverConfigurationFeature.class).ifPresent(driverConfiguration -> {
                String url = null;
                String driver = "org.testcontainers.jdbc.ContainerDatabaseDriver";
                String dependencyArtifactId = null;
                if (driverFeature instanceof MySQL) {
                    url = "jdbc:tc:mysql:8:///db";
                    dependencyArtifactId = "mysql";
                } else if (driverFeature instanceof PostgreSQL) {
                    url = "jdbc:tc:postgresql:12:///postgres";
                    dependencyArtifactId = "postgresql";
                } else if (driverFeature instanceof MariaDB) {
                    url = "jdbc:tc:mariadb:10:///db";
                    dependencyArtifactId = "mariadb";
                } else if (driverFeature instanceof SQLServer) {
                    url = "jdbc:tc:sqlserver:2019-CU4-ubuntu-16.04://databaseName=tempdb";
                    generatorContext.addTemplate("sqlserverEula", new StringTemplate("src/test/resources/container-license-acceptance.txt", "mcr.microsoft.com/mssql/server:2019-CU4-ubuntu-16.04"));
                    dependencyArtifactId = "mssqlserver";
                } else if (driverFeature instanceof Oracle) {
                    url = "jdbc:tc:oracle:thin:@/xe";
                    generatorContext.addTemplate("testContainersProperties", new PropertiesTemplate("src/test/resources/testcontainers.properties", Collections.singletonMap("oracle.container.image", "wnameless/oracle-xe-11g-r2")));
                    dependencyArtifactId = "oracle-xe";
                }

                if (url != null) {
                    Configuration testConfig = generatorContext.getConfiguration("test", ApplicationConfiguration.testConfig());
                    testConfig.put(driverConfiguration.getUrlKey(), url);
                    testConfig.put(driverConfiguration.getDriverKey(), driver);
                }
                if (dependencyArtifactId != null) {
                    generatorContext.addDependency(Dependency.builder()
                            .groupId("org.testcontainers")
                            .artifactId(dependencyArtifactId)
                            .test());
                }
            });
        });
        if (generatorContext.getTestFramework() == TestFramework.SPOCK) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId("org.testcontainers")
                    .artifactId("spock")
                    .test());
        } else if (generatorContext.getTestFramework() == TestFramework.JUNIT) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId("org.testcontainers")
                    .artifactId("junit-jupiter")
                    .test());
        }
        if (generatorContext.isFeaturePresent(MongoFeature.class)) {
            generatorContext.addDependency(Dependency.builder()
                    .groupId("org.testcontainers")
                    .artifactId("mongodb")
                    .test());
        }
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

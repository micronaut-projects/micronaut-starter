/*
 * Copyright 2017-2024 original authors
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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.feature.config.ApplicationConfiguration;
import io.micronaut.starter.feature.config.Configuration;
import io.micronaut.starter.feature.database.r2dbc.R2dbc;
import io.micronaut.starter.feature.testcontainers.ContributingTestContainerDependency;
import io.micronaut.starter.options.TestFramework;
import io.micronaut.starter.template.StringTemplate;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class TestContainers implements Feature {
    public static final String NAME = "testcontainers";
    private static final String ARTIFACT_ID_TESTCONTAINERS = "testcontainers";

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Testcontainers";
    }

    @Override
    public String getDescription() {
        return "Use Testcontainers to run a database or other software in a Docker container for tests";
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getFeatures().getFeatures()
                .stream()
                .filter(ContributingTestContainerDependency.class::isInstance)
                .forEach(f -> ((ContributingTestContainerDependency) f).testContainersDependencies().forEach(generatorContext::addDependency));
        generatorContext.addDependency(ContributingTestContainerDependency.testContainerDependency(ARTIFACT_ID_TESTCONTAINERS));

        generatorContext.getFeature(DatabaseDriverFeature.class).ifPresent(driverFeature -> {
            generatorContext.getFeature(R2dbc.class).ifPresent(driverConfiguration -> {
                if (driverFeature instanceof SQLServer) {
                    generatorContext.addTemplate("sqlserverEula", new StringTemplate("src/test/resources/container-license-acceptance.txt", "mcr.microsoft.com/mssql/server:2019-CU16-GDR1-ubuntu-20.04"));
                }
                r2dbcUrlForDatabaseDriverFeature(driverFeature).ifPresent(url -> {
                    Configuration testConfig = generatorContext.getConfiguration("test", ApplicationConfiguration.testConfig());
                    testConfig.put(driverConfiguration.getUrlKey(), url);
                });
                generatorContext.addDependency(ContributingTestContainerDependency.testContainerDependency("r2dbc"));
                // TestContainers requires the database module, a jdbc driver AND the r2dbc module: see https://www.testcontainers.org/modules/databases/r2dbc/
                driverFeature.getJavaClientDependency().ifPresent(d -> generatorContext.addDependency(d.testRuntime()));
                artifactIdForDriverFeature(driverFeature).ifPresent(dependencyArtifactId ->
                        generatorContext.addDependency(ContributingTestContainerDependency.testContainerDependency(dependencyArtifactId)));
            });
            generatorContext.getFeature(DatabaseDriverConfigurationFeature.class).ifPresent(driverConfiguration -> {
                String driver = "org.testcontainers.jdbc.ContainerDatabaseDriver";
                if (driverFeature instanceof SQLServer) {
                    generatorContext.addTemplate("sqlserverEula", new StringTemplate("src/test/resources/container-license-acceptance.txt", "mcr.microsoft.com/mssql/server:2019-CU16-GDR1-ubuntu-20.04"));
                }
                urlForDatabaseDriverFeature(driverFeature).ifPresent(url -> {
                    Configuration testConfig = generatorContext.getConfiguration("test", ApplicationConfiguration.testConfig());
                    testConfig.put(driverConfiguration.getUrlKey(), url);
                    testConfig.put(driverConfiguration.getDriverKey(), driver);
                });
                artifactIdForDriverFeature(driverFeature).ifPresent(dependencyArtifactId ->
                        generatorContext.addDependency(ContributingTestContainerDependency.testContainerDependency(dependencyArtifactId)));
            });
            generatorContext.getFeature(HibernateReactiveFeature.class).ifPresent(hibernateReactiveFeature -> {
                urlForDatabaseDriverFeature(driverFeature).ifPresent(url -> {
                    Configuration testConfig = generatorContext.getConfiguration("test", ApplicationConfiguration.testConfig());
                    testConfig.put(hibernateReactiveFeature.getUrlKey(), url);
                });
                artifactIdForDriverFeature(driverFeature)
                        .ifPresent(dependencyArtifactId -> generatorContext.addDependency(ContributingTestContainerDependency.testContainerDependency(dependencyArtifactId)));
            });
        });
        testContainerArtifactIdByTestFramework(generatorContext.getTestFramework()).ifPresent(testArtifactId -> {
            generatorContext.addDependency(ContributingTestContainerDependency.testContainerDependency(testArtifactId));
        });
    }

    @NonNull
    private static Optional<String> testContainerArtifactIdByTestFramework(TestFramework testFramework) {
        if (testFramework == TestFramework.SPOCK) {
            return Optional.of("spock");
        } else if (testFramework == TestFramework.JUNIT) {
            return Optional.of("junit-jupiter");
        }
        return Optional.empty();
    }

    @NonNull
    public static Optional<String> artifactIdForDriverFeature(@NonNull DatabaseDriverFeature driverFeature) {
        if (driverFeature instanceof MySQL) {
            return Optional.of("mysql");
        } else if (driverFeature instanceof PostgreSQL) {
            return Optional.of("postgresql");
        } else if (driverFeature instanceof MariaDB) {
            return Optional.of("mariadb");
        } else if (driverFeature instanceof SQLServer) {
            return Optional.of("mssqlserver");
        } else if (driverFeature instanceof Oracle) {
            return Optional.of("oracle-xe");
        }
        return Optional.empty();
    }

    @NonNull
    private static Optional<String> r2dbcUrlForDatabaseDriverFeature(@NonNull DatabaseDriverFeature driverFeature) {
        if (driverFeature instanceof MySQL) {
            return Optional.of("r2dbc:tc:mysql:///db?TC_IMAGE_TAG=8");
        } else if (driverFeature instanceof PostgreSQL) {
            return Optional.of("r2dbc:tc:postgresql:///db?TC_IMAGE_TAG=12");
        } else if (driverFeature instanceof MariaDB) {
            return Optional.of("r2dbc:tc:mariadb:///db?TC_IMAGE_TAG=10");
        } else if (driverFeature instanceof SQLServer) {
            return Optional.of("r2dbc:tc:sqlserver:///db?TC_IMAGE_TAG=2019-CU16-GDR1-ubuntu-20.04");
        }
        return Optional.empty();
    }

    @NonNull
    private static Optional<String> urlForDatabaseDriverFeature(@NonNull DatabaseDriverFeature driverFeature) {
        if (driverFeature instanceof MySQL) {
            return Optional.of("jdbc:tc:mysql:8:///db");

        } else if (driverFeature instanceof PostgreSQL) {
            return Optional.of("jdbc:tc:postgresql:14:///postgres");

        } else if (driverFeature instanceof MariaDB) {
            return Optional.of("jdbc:tc:mariadb:10:///db");

        } else if (driverFeature instanceof SQLServer) {
            return Optional.of("jdbc:tc:sqlserver:2019-CU16-GDR1-ubuntu-20.04://databaseName=tempdb");
        } else if (driverFeature instanceof Oracle) {
            return Optional.of("jdbc:tc:oracle:thin:@/xe");
        }
        return Optional.empty();
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public int getOrder() {
        return FeaturePhase.TEST.getOrder();
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://www.testcontainers.org/";
    }
}

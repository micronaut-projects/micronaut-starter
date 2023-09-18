/*
 * Copyright 2017-2023 original authors
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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import jakarta.inject.Singleton;

@Singleton
public class DataSpringJdbcFeature implements Feature {
    public static final String NAME = "data-spring-jdbc";
    public static final String TXMGR_CONFIG_KEY = "datasources.default.transaction-manager";
    public static final String TXMGR_CONFIG_VALUE = "springJdbc";
    public static final String MICRONAUT_DATA_SPRING_JDBC_ARTIFACT = "micronaut-data-spring-jdbc";

    private static final Dependency MICRONAUT_DATA_SPRING_JDBC = MicronautDependencyUtils
            .dataDependency()
            .artifactId(MICRONAUT_DATA_SPRING_JDBC_ARTIFACT)
            .compile()
            .build();

    private final DataJdbc dataJdbc;

    public DataSpringJdbcFeature(DataJdbc dataJdbc) {
        this.dataJdbc = dataJdbc;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "Micronaut Data Spring JDBC";
    }

    @Override
    public String getDescription() {
        return "Adds support for Micronaut Data Spring JDBC";
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(DataJdbc.class)) {
            featureContext.addFeature(dataJdbc);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.addDependency(MICRONAUT_DATA_SPRING_JDBC);
        generatorContext.getConfiguration().addNested(TXMGR_CONFIG_KEY, TXMGR_CONFIG_VALUE);
    }

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-data/latest/guide/#spring";
    }
}

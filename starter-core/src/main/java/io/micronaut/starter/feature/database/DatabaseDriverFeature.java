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
package io.micronaut.starter.feature.database;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.OneOfFeature;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;
import io.micronaut.starter.feature.database.r2dbc.R2dbc;
import io.micronaut.starter.feature.database.r2dbc.R2dbcFeature;
import io.micronaut.starter.feature.migration.MigrationFeature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class DatabaseDriverFeature
        implements OneOfFeature, DatabaseDriverFeatureDependencies {

    private final JdbcFeature jdbcFeature;
    private final TestContainers testContainers;

    public DatabaseDriverFeature() {
        this(null, null);
    }

    public DatabaseDriverFeature(JdbcFeature jdbcFeature, TestContainers testContainers) {
        this.jdbcFeature = jdbcFeature;
        this.testContainers = testContainers;
    }

    @Override
    public Class<?> getFeatureClass() {
        return DatabaseDriverFeature.class;
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(TestContainers.class) && testContainers != null) {
            featureContext.addFeature(testContainers);
        }
        if (shouldAddJdbcFeature(featureContext)) {
            featureContext.addFeature(jdbcFeature);
        }
    }

    private boolean shouldAddJdbcFeature(FeatureContext featureContext) {
        return !featureContext.isPresent(JdbcFeature.class)
                && !featureContext.isPresent(R2dbcFeature.class)
                && !featureContext.isPresent(DataHibernateReactive.class)
                && !featureContext.isPresent(HibernateReactiveJpa.class)
                && jdbcFeature != null;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    public abstract boolean embedded();

    public abstract String getJdbcUrl();

    public abstract String getR2dbcUrl();

    public abstract String getDriverClass();

    public abstract String getDefaultUser();

    public abstract String getDefaultPassword();

    public abstract String getDataDialect();

    public Map<String, Object> getAdditionalConfig() {
        return Collections.emptyMap();
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        parseDependencies(generatorContext).forEach(generatorContext::addDependency);
    }

    @NonNull
    protected List<Dependency.Builder> parseDependencies(GeneratorContext generatorContext) {
        List<Dependency.Builder> dependencies = new ArrayList<>();
        if (generatorContext.isFeaturePresent(R2dbc.class)) {
            getR2DbcDependency().ifPresent(dependencies::add);
            if (!generatorContext.isFeaturePresent(MigrationFeature.class)) {
                return dependencies;
            }
        }
        if (generatorContext.getFeatures().hasFeature(DataHibernateReactive.class) || generatorContext.getFeatures().hasFeature(HibernateReactiveJpa.class)) {
            getHibernateReactiveJavaClientDependency().ifPresent(dependencies::add);
        } else {
            getJavaClientDependency().ifPresent(dependencies::add);
        }
        return dependencies;
    }
}

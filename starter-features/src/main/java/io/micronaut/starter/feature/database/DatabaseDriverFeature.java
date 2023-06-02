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
import io.micronaut.starter.feature.testresources.DbType;
import io.micronaut.starter.feature.testresources.EaseTestingFeature;
import io.micronaut.starter.feature.testresources.TestResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class DatabaseDriverFeature extends EaseTestingFeature implements OneOfFeature, DatabaseDriverFeatureDependencies {

    private final JdbcFeature jdbcFeature;

    public DatabaseDriverFeature() {
        this(null, null, null);
    }

    public DatabaseDriverFeature(JdbcFeature jdbcFeature,
                                 TestContainers testContainers,
                                 TestResources testResources) {
        super(testContainers, testResources);
        this.jdbcFeature = jdbcFeature;
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
        super.processSelectedFeatures(featureContext);
        if (shouldAddJdbcFeature(featureContext)) {
            featureContext.addFeature(jdbcFeature);
        }
    }

    private boolean shouldAddJdbcFeature(FeatureContext featureContext) {
        return !featureContext.isPresent(JdbcFeature.class)
                && !featureContext.isPresent(R2dbcFeature.class)
                && !hasHibernateReactiveWithoutMigration(featureContext)
                && jdbcFeature != null;
    }

    private boolean hasHibernateReactiveWithoutMigration(FeatureContext featureContext) {
        return featureContext.isPresent(HibernateReactiveFeature.class) && !featureContext.isPresent(MigrationFeature.class);
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

    @NonNull
    public Optional<DbType> getDbType() {
        return Optional.empty();
    }

    public Map<String, Object> getAdditionalConfig(GeneratorContext generatorContext) {
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
            if (generatorContext.isFeaturePresent(MigrationFeature.class)) {
                getJavaClientDependency().ifPresent(dependencies::add);
            }
        } else {
            getJavaClientDependency().ifPresent(dependencies::add);
        }
        return dependencies;
    }
}

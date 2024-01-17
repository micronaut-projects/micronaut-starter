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
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MavenCoordinate;
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
import io.micronaut.starter.feature.testresources.TestResourcesAdditionalModulesProvider;

import java.util.*;

import static io.micronaut.starter.build.dependencies.MicronautDependencyUtils.GROUP_ID_MICRONAUT_TESTRESOURCES;

public abstract class DatabaseDriverFeature extends EaseTestingFeature implements OneOfFeature, DatabaseDriverFeatureDependencies, TestResourcesAdditionalModulesProvider {

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

    @Override
    @NonNull
    public List<String> getTestResourcesAdditionalModules(@NonNull GeneratorContext generatorContext) {
        if (
                (!generatorContext.isFeaturePresent(Data.class) || generatorContext.isFeaturePresent(HibernateReactiveFeature.class))
        ) {
            return getDbType().map(dbType -> {
                if (generatorContext.isFeaturePresent(R2dbc.class)) {
                    return Collections.singletonList(dbType.getR2dbcTestResourcesModuleName());
                } else if (generatorContext.isFeaturePresent(HibernateReactiveFeature.class)) {
                    return Collections.singletonList(dbType.getHibernateReactiveTestResourcesModuleName());
                } else {
                    return Collections.singletonList(dbType.getJdbcTestResourcesModuleName());
                }
            }).orElseGet(Collections::emptyList);
        }
        return Collections.emptyList();
    }

    @Override
    @NonNull
    public List<MavenCoordinate> getTestResourcesDependencies(@NonNull GeneratorContext generatorContext) {
        List<MavenCoordinate> dependencies = new ArrayList<>();
        if (
                (!generatorContext.isFeaturePresent(Data.class) || generatorContext.isFeaturePresent(HibernateReactiveFeature.class) || generatorContext.isFeaturePresent(R2dbc.class))
        ) {
            getDbType()
                    .map(dbType -> {
                        if (generatorContext.isFeaturePresent(R2dbc.class)) {
                            return dbType.getR2dbcTestResourcesModuleName();
                        } else if (generatorContext.isFeaturePresent(HibernateReactiveFeature.class)) {
                            return dbType.getHibernateReactiveTestResourcesModuleName();
                        } else {
                            return dbType.getJdbcTestResourcesModuleName();
                        }
                    }).map(resourceName -> new MavenCoordinate(GROUP_ID_MICRONAUT_TESTRESOURCES, "micronaut-test-resources-" + resourceName, null))
                    .ifPresent(dependencies::add);
        }
        if ((generatorContext.isFeaturePresent(HibernateReactiveFeature.class) || generatorContext.isFeaturePresent(R2dbc.class))
                && generatorContext.isFeaturePresent(DatabaseDriverFeature.class)
                && !generatorContext.isFeaturePresent(MigrationFeature.class)
        ) {
            generatorContext.getFeature(DatabaseDriverFeature.class)
                    .flatMap(DatabaseDriverFeatureDependencies::getJavaClientDependency)
                    .map(Dependency.Builder::build)
                    .ifPresent(driver -> dependencies.add(new MavenCoordinate(driver.getGroupId(), driver.getArtifactId(), null)));
        }
        return dependencies;
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

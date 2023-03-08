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
package io.micronaut.starter.feature.database.r2dbc;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.DatabaseDriverFeature;

import io.micronaut.starter.feature.database.jdbc.Hikari;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;
import io.micronaut.starter.feature.migration.MigrationFeature;
import io.micronaut.starter.feature.testresources.TestResources;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.LinkedHashMap;
import java.util.Map;

@Singleton
public class R2dbc implements R2dbcFeature {

    public static final String NAME = "r2dbc";

    private static final Dependency DEPENDENCY_MICRONAUT_R2DBC_CORE = MicronautDependencyUtils.r2dbcDependency()
            .artifactId("micronaut-r2dbc-core")
            .compile()
            .build();

    private static final String PREFIX = "r2dbc.datasources.default.";
    private static final String URL_KEY = PREFIX + "url";
    private static final String USERNAME_KEY = PREFIX + "username";
    private static final String PASSWORD_KEY = PREFIX + "password";

    private final DatabaseDriverFeature defaultDbFeature;
    private final Hikari hikari;

    @Deprecated
    public R2dbc(DatabaseDriverFeature defaultDbFeature) {
        this(defaultDbFeature, new Hikari(defaultDbFeature));
    }

    @Inject
    public R2dbc(DatabaseDriverFeature defaultDbFeature, Hikari hikari) {
        this.defaultDbFeature = defaultDbFeature;
        this.hikari = hikari;
    }

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return "R2DBC";
    }

    @Override
    @NonNull
    public String getDescription() {
        return "R2DBC - Reactive Database Connectivity";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(DatabaseDriverFeature.class)) {
            featureContext.addFeature(defaultDbFeature);
        }
        if (featureContext.isPresent(MigrationFeature.class) && !featureContext.isPresent(JdbcFeature.class)) {
            featureContext.addFeature(hikari);
        }
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }

    @Nullable
    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-r2dbc/latest/guide/";
    }

    @Nullable
    @Override
    public String getThirdPartyDocumentation() {
        return "https://r2dbc.io";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        DatabaseDriverFeature dbFeature = generatorContext.getRequiredFeature(DatabaseDriverFeature.class);
        if (!generatorContext.isFeaturePresent(TestResources.class)) {
            Map<String, Object> rdbcConfig = new LinkedHashMap<>();
            rdbcConfig.put(getUrlKey(), dbFeature.getR2dbcUrl());
            rdbcConfig.put(USERNAME_KEY, dbFeature.getDefaultUser());
            rdbcConfig.put(PASSWORD_KEY, dbFeature.getDefaultPassword());
            generatorContext.getConfiguration().putAll(rdbcConfig);
        } else {
            dbFeature.getDbType().ifPresent(type -> generatorContext.getConfiguration().addNested("r2dbc.datasources.default.db-type", type.toString()));
        }
        if (!generatorContext.isFeaturePresent(DataR2dbc.class)) {
            generatorContext.addDependency(DEPENDENCY_MICRONAUT_R2DBC_CORE);
        }
    }

    public String getUrlKey() {
        return URL_KEY;
    }
}

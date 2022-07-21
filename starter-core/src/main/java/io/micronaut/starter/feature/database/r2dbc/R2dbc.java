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
package io.micronaut.starter.feature.database.r2dbc;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.DatabaseDriverFeature;

import jakarta.inject.Singleton;
import java.util.LinkedHashMap;
import java.util.Map;

@Singleton
public class R2dbc implements R2dbcFeature {
    private static final Dependency.Builder DEPENDENCY_MICRONAUT_R2DBC_CORE = Dependency.builder()
            .groupId("io.micronaut.r2dbc")
            .artifactId("micronaut-r2dbc-core")
            .compile();

    private static final String PREFIX = "r2dbc.datasources.default.";
    private static final String URL_KEY = PREFIX + "url";
    private static final String USERNAME_KEY = PREFIX + "username";
    private static final String PASSWORD_KEY = PREFIX + "password";

    private final DatabaseDriverFeature defaultDbFeature;

    public R2dbc(DatabaseDriverFeature defaultDbFeature) {
        this.defaultDbFeature = defaultDbFeature;
    }

    @NonNull
    @Override
    public String getName() {
        return "r2dbc";
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
        generatorContext.getFeature(DatabaseDriverFeature.class).ifPresent(dbFeature -> {
            Map<String, Object> rdbcConfig = new LinkedHashMap<>();
            rdbcConfig.put(getUrlKey(), dbFeature.getR2dbcUrl());
            rdbcConfig.put(USERNAME_KEY, dbFeature.getDefaultUser());
            rdbcConfig.put(PASSWORD_KEY, dbFeature.getDefaultPassword());
            generatorContext.getConfiguration().putAll(rdbcConfig);
        });
        if (!generatorContext.isFeaturePresent(DataR2dbc.class)) {
            generatorContext.addDependency(DEPENDENCY_MICRONAUT_R2DBC_CORE);
        }
    }

    public String getUrlKey() {
        return URL_KEY;
    }
}

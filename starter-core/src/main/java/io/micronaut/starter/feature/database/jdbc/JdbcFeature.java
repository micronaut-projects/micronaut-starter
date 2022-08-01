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
package io.micronaut.starter.feature.database.jdbc;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.FeaturePhase;
import io.micronaut.starter.feature.OneOfFeature;
import io.micronaut.starter.feature.database.DatabaseDriverConfigurationFeature;
import io.micronaut.starter.feature.database.DatabaseDriverFeature;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class JdbcFeature implements OneOfFeature, DatabaseDriverConfigurationFeature {

    private static final String PREFIX = "datasources.default.";
    public static final String PROPERTY_DATASOURCES_DEFAULT_DB_TYPE = PREFIX + "db-type";
    private static final String URL_KEY = PREFIX + "url";
    private static final String DRIVER_KEY = PREFIX + "driverClassName";
    private static final String USERNAME_KEY = PREFIX + "username";
    private static final String PASSWORD_KEY = PREFIX + "password";

    private final DatabaseDriverFeature defaultDbFeature;

    public JdbcFeature(DatabaseDriverFeature defaultDbFeature) {
        this.defaultDbFeature = defaultDbFeature;
    }

    @Override
    public int getOrder() {
        return FeaturePhase.LOW.getOrder();
    }

    @Override
    public Class<?> getFeatureClass() {
        return JdbcFeature.class;
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(DatabaseDriverFeature.class)) {
            featureContext.addFeature(defaultDbFeature);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        generatorContext.getFeature(DatabaseDriverFeature.class).ifPresent(dbFeature -> {
            Map<String, Object> jdbcConfig = new LinkedHashMap<>();
            applyDefaultConfig(generatorContext, dbFeature, jdbcConfig);
            generatorContext.getConfiguration().addNested(jdbcConfig);
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

    @Override
    public String getMicronautDocumentation() {
        return "https://micronaut-projects.github.io/micronaut-sql/latest/guide/index.html#jdbc";
    }

    public String getUrlKey() {
        return URL_KEY;
    }

    public String getDriverKey() {
        return DRIVER_KEY;
    }

    public String getUsernameKey() {
        return USERNAME_KEY;
    }

    public String getPasswordKey() {
        return PASSWORD_KEY;
    }
}

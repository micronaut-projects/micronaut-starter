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

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.LanguageSpecificFeature;
import io.micronaut.starter.feature.other.HibernateValidator;
import io.micronaut.starter.options.Language;

import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class HibernateGorm implements LanguageSpecificFeature, DatabaseDriverConfigurationFeature {

    private static final String PREFIX = "dataSource.";
    private static final String URL_KEY = PREFIX + "url";
    private static final String DRIVER_KEY = PREFIX + "driverClassName";
    private static final String USERNAME_KEY = PREFIX + "username";
    private static final String PASSWORD_KEY = PREFIX + "password";

    private final DatabaseDriverFeature defaultDbFeature;
    private final HibernateValidator hibernateValidator;

    public HibernateGorm(DatabaseDriverFeature defaultDbFeature, HibernateValidator hibernateValidator) {
        this.defaultDbFeature = defaultDbFeature;
        this.hibernateValidator = hibernateValidator;
    }

    @Override
    public boolean isPreview() {
        return true;
    }

    @Override
    public String getName() {
        return "hibernate-gorm";
    }

    @Override
    public String getTitle() {
        return "GORM for Hibernate";
    }

    @Override
    public String getDescription() {
        return "Adds support for GORM persistence framework";
    }

    @Override
    public Language[] getRequiredLanguages() {
        return new Language[]{Language.GROOVY};
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(DatabaseDriverFeature.class)) {
            featureContext.addFeature(defaultDbFeature);
        }
        if (!featureContext.isPresent(HibernateValidator.class)) {
            featureContext.addFeature(hibernateValidator);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Map<String, Object> config = generatorContext.getConfiguration();
        DatabaseDriverFeature dbFeature = generatorContext.getRequiredFeature(DatabaseDriverFeature.class);
        config.put(getUrlKey(), dbFeature.getJdbcUrl());
        config.put(getDriverKey(), dbFeature.getDriverClass());
        config.put(getUsernameKey(), dbFeature.getDefaultUser());
        config.put(getPasswordKey(), dbFeature.getDefaultPassword());
        config.put("dataSource.pooled", true);
        config.put("dataSource.jmxExport", true);
        config.put("hibernate.hbm2ddl.auto", "update");
        config.put("hibernate.cache.queries", false);
        config.put("hibernate.cache.use_second_level_cache", false);
        config.put("hibernate.cache.use_query_cache", false);
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getUrlKey() {
        return URL_KEY;
    }

    @Override
    public String getDriverKey() {
        return DRIVER_KEY;
    }

    @Override
    public String getUsernameKey() {
        return USERNAME_KEY;
    }

    @Override
    public String getPasswordKey() {
        return PASSWORD_KEY;
    }

    @Override
    public String getCategory() {
        return Category.DATABASE;
    }
}

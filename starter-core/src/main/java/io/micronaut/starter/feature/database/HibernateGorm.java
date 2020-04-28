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
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.other.HibernateValidator;
import io.micronaut.starter.options.Language;

import javax.inject.Singleton;
import java.util.Map;
import java.util.Optional;

@Singleton
public class HibernateGorm implements Feature {

    private final H2 h2;
    private final HibernateValidator hibernateValidator;

    public HibernateGorm(H2 h2, HibernateValidator hibernateValidator) {
        this.h2 = h2;
        this.hibernateValidator = hibernateValidator;
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
    public Optional<Language> getRequiredLanguage() {
        return Optional.of(Language.GROOVY);
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(H2.class)) {
            featureContext.addFeature(h2);
        }
        if (!featureContext.isPresent(HibernateValidator.class)) {
            featureContext.addFeature(hibernateValidator);
        }
    }

    @Override
    public void apply(GeneratorContext generatorContext) {
        Map<String, Object> config = generatorContext.getConfiguration();
        config.put("dataSource.url", "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE");
        config.put("dataSource.pooled", true);
        config.put("dataSource.jmxExport", true);
        config.put("dataSource.driverClassName", "org.h2.Driver");
        config.put("dataSource.username", "sa");
        config.put("dataSource.password", "");
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
    public String getCategory() {
        return Category.DATABASE;
    }
}

package io.micronaut.starter.feature.database;

import io.micronaut.starter.command.CommandContext;
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
    public String getDescription() {
        return "Adds support for GORM persistence framework";
    }

    @Override
    public Optional<Language> getRequiredLanguage() {
        return Optional.of(Language.groovy);
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
    public void apply(CommandContext commandContext) {
        Map<String, Object> config = commandContext.getConfiguration();
        config.put("hibernate.hbm2ddl.auto", "update");
        config.put("hibernate.cache.queries", false);
        config.put("hibernate.cache.use_second_level_cache", false);
        config.put("hibernate.cache.use_query_cache", false);
        config.put("dataSource.url", "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE");
        config.put("dataSource.pooled", true);
        config.put("dataSource.jmxExport", true);
        config.put("dataSource.driverClassName", "org.h2.Driver");
        config.put("dataSource.username", "sa");
        config.put("dataSource.password", "");
    }
}

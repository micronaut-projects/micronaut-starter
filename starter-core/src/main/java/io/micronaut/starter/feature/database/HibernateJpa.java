package io.micronaut.starter.feature.database;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;

import javax.inject.Singleton;

@Singleton
public class HibernateJpa implements Feature {

    private final JdbcFeature jdbcFeature;

    public HibernateJpa(JdbcFeature jdbcFeature) {
        this.jdbcFeature = jdbcFeature;
    }

    @Override
    public String getName() {
        return "hibernate-jpa";
    }

    @Override
    public String getDescription() {
        return "Adds support for Hibernate/JPA";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        if (!featureContext.isPresent(JdbcFeature.class)) {
            featureContext.addFeature(jdbcFeature);
        }
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().putAll(ConfigurationHelper.JDBC_H2);
        commandContext.getConfiguration().putAll(ConfigurationHelper.JPA_DDL);
    }
}

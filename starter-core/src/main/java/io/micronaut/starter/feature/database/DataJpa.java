package io.micronaut.starter.feature.database;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.database.jdbc.JdbcFeature;
import io.micronaut.starter.feature.database.jdbc.Tomcat;

import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class DataJpa implements DataFeature {

    private final Data data;
    private final JdbcFeature jdbcFeature;

    public DataJpa(Data data, JdbcFeature jdbcFeature) {
        this.data = data;
        this.jdbcFeature = jdbcFeature;
    }

    @Override
    public String getName() {
        return "data-jpa";
    }

    @Override
    public String getDescription() {
        return "Adds support for Micronaut Data Hibernate/JPA";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeature(data);
        if (!featureContext.isPresent(JdbcFeature.class)) {
            featureContext.addFeature(jdbcFeature);
        }
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().remove("datasources.default");
        commandContext.getConfiguration().putAll(ConfigurationHelper.JDBC_H2);
        commandContext.getConfiguration().putAll(getDatasourceConfig());
        commandContext.getConfiguration().putAll(ConfigurationHelper.JPA_DDL);
    }
}

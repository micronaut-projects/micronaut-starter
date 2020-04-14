package io.micronaut.starter.feature.database;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.jdbc.JdbcFeature;
import io.micronaut.starter.feature.jdbc.Tomcat;

import javax.inject.Singleton;

@Singleton
public class DataJdbc implements DataFeature {

    private final Data data;
    private final Tomcat tomcat;

    public DataJdbc(Data data, Tomcat tomcat) {
        this.data = data;
        this.tomcat = tomcat;
    }

    @Override
    public String getName() {
        return "data-jdbc";
    }

    @Override
    public String getDescription() {
        return "Adds support for Micronaut Data JDBC";
    }

    @Override
    public void processSelectedFeatures(FeatureContext featureContext) {
        featureContext.addFeature(data);
        if (!featureContext.isPresent(JdbcFeature.class)) {
            featureContext.addFeature(tomcat);
        }
    }

    @Override
    public void apply(CommandContext commandContext) {
        commandContext.getConfiguration().putAll(getDatasourceConfig());
    }
}

package io.micronaut.starter.feature.database;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.jdbc.JdbcFeature;
import io.micronaut.starter.feature.jdbc.Tomcat;

import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class DataJpa implements DataFeature {

    private final Data data;
    private final Tomcat tomcat;

    public DataJpa(Data data, Tomcat tomcat) {
        this.data = data;
        this.tomcat = tomcat;
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
            featureContext.addFeature(tomcat);
        }
    }

    @Override
    public void apply(CommandContext commandContext) {
        Map<String, Object> conf = getDatasourceConfig();
        conf.put("jpa.default.properties.hibernate.hbm2ddl.auto", "update");
        commandContext.getConfiguration().putAll(conf);
    }
}

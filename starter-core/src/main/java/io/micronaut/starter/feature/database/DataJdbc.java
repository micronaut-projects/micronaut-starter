package io.micronaut.starter.feature.database;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.jdbc.JdbcFeature;
import io.micronaut.starter.feature.jdbc.Tomcat;

import java.util.Map;

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
        final String prefix = "datasources.default.";
        Map<String, Object> conf = commandContext.getConfiguration();
        conf.put(prefix + "url", "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE");
        conf.put(prefix + "driverClassName", "org.h2.Driver");
        conf.put(prefix + "username", "sa");
        conf.put(prefix + "password", "''");
        conf.put(prefix + "schema-generate", "CREATE_DROP");
        conf.put(prefix + "dialect", "H2");
    }
}

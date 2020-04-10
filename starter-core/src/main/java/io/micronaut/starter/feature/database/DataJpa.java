package io.micronaut.starter.feature.database;

import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.FeatureContext;
import io.micronaut.starter.feature.jdbc.JdbcFeature;
import io.micronaut.starter.feature.jdbc.Tomcat;

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
}

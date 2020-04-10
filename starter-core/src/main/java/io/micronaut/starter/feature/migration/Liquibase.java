package io.micronaut.starter.feature.migration;

import javax.inject.Singleton;

@Singleton
public class Liquibase implements MigrationFeature {

    @Override
    public String getName() {
        return "liquibase";
    }

    @Override
    public String getDescription() {
        return "Adds support for Liquibase database migrations (http://www.liquibase.org/)";
    }
}

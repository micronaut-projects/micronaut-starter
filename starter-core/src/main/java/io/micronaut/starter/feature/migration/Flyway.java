package io.micronaut.starter.feature.migration;

import javax.inject.Singleton;

@Singleton
public class Flyway implements MigrationFeature {

    @Override
    public String getName() {
        return "flyway";
    }

    @Override
    public String getDescription() {
        return "Adds support for Flyway database migrations (https://flywaydb.org/)";
    }
}


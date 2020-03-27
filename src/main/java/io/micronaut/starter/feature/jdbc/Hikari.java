package io.micronaut.starter.feature.jdbc;

import io.micronaut.starter.command.CommandContext;

public class Hikari implements JdbcFeature {

    @Override
    public String getName() {
        return "jdbc-hikari";
    }

}

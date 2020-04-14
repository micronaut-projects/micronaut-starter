package io.micronaut.starter.feature.database;

import io.micronaut.starter.command.CommandContext;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class H2 implements Feature {

    @Override
    public String getName() {
        return "h2";
    }

    @Override
    public boolean isVisible() {
        return false;
    }

}

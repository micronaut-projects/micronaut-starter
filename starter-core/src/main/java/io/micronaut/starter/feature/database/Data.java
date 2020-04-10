package io.micronaut.starter.feature.database;

import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class Data implements Feature {

    @Override
    public String getName() {
        return "data";
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}

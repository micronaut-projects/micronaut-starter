package io.micronaut.starter.feature.other;

import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class Management implements Feature {

    @Override
    public String getName() {
        return "management";
    }
}

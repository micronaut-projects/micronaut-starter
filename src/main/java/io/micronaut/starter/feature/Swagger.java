package io.micronaut.starter.feature;

import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class Swagger implements Feature {

    @Override
    public String getName() {
        return "swagger";
    }

}

package io.micronaut.starter.feature.server;

import javax.inject.Singleton;

@Singleton
public class Undertow implements ServerFeature {

    @Override
    public String getName() {
        return "undertow-server";
    }
}

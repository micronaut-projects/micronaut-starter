package io.micronaut.starter.feature.server;

import javax.inject.Singleton;

@Singleton
public class Jetty implements ServerFeature {

    @Override
    public String getName() {
        return "jetty-server";
    }
}

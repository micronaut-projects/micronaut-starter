package io.micronaut.starter.feature.server;

import javax.inject.Singleton;

@Singleton
public class Tomcat implements ServerFeature {

    @Override
    public String getName() {
        return "tomcat-server";
    }
}

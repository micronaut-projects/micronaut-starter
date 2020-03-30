package io.micronaut.starter.feature.jdbc;

import javax.inject.Singleton;

@Singleton
public class Tomcat implements JdbcFeature {

    @Override
    public String getName() {
        return "jdbc-tomcat";
    }
}

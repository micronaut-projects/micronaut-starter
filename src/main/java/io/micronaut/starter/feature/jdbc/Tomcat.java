package io.micronaut.starter.feature.jdbc;

public class Tomcat implements JdbcFeature {

    @Override
    public String getName() {
        return "jdbc-tomcat";
    }
}

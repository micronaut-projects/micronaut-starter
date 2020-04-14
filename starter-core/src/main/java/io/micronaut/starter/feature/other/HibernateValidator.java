package io.micronaut.starter.feature.other;

import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class HibernateValidator implements Feature {

    @Override
    public String getName() {
        return "hibernate-validator";
    }

    @Override
    public String getDescription() {
        return "Adds support for the Hibernate validator";
    }
}

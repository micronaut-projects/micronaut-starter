package io.micronaut.starter.feature.security;

import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class SecurityAnnotations implements Feature {

    @Override
    public String getName() {
        return "security-annotations";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}

package io.micronaut.starter.feature.security;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.feature.Category;
import io.micronaut.starter.feature.Feature;

import javax.inject.Singleton;

@Singleton
public class SecurityLdap implements Feature {

    @NonNull
    @Override
    public String getName() {
        return "security-ldap";
    }

    @Override
    public String getTitle() {
        return "Micronaut Security LDAP";
    }

    @Override
    public String getDescription() {
        return "Adds support for authentication with LDAP servers";
    }

    @Override
    public boolean supports(ApplicationType applicationType) {
        return true;
    }

    @Override
    public String getCategory() {
        return Category.SECURITY;
    }
}

package io.micronaut.starter.feature.build.gradle;

import io.micronaut.core.annotation.NonNull;

public abstract class SystemPropertyBuildCacheCredentialsProvider implements BuildCacheCredentialsProvider {

    @NonNull
    abstract String getUsernameSystemProperty();

    @NonNull
    abstract String getPasswordSystemProperty();

    @Override
    @NonNull
    public Credentials provideCredentails() {
        return new Credentials() {
            @Override
            public String getUsername() {
                return "System.getProperty(\"" + getUsernameSystemProperty() +"\")";
            }

            @Override
            public String getPassword() {
                return "System.getProperty(\"" + getPasswordSystemProperty() +"\")";
            }
        };
    }

}

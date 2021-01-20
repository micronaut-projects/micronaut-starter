package io.micronaut.starter.feature.config;

import edu.umd.cs.findbugs.annotations.NonNull;

public class ApplicationConfiguration extends Configuration {

    public ApplicationConfiguration(@NonNull String sourceSet, @NonNull String environment) {
        super(sourceSet, "application-" + environment, "application-config-" + environment);
    }

    public ApplicationConfiguration(@NonNull String environment) {
        this("main", environment);
    }

    public ApplicationConfiguration() {
        super("main", "application", "application-config");
    }

    public static ApplicationConfiguration testConfig() {
        return new ApplicationConfiguration("test", "test");
    }
}

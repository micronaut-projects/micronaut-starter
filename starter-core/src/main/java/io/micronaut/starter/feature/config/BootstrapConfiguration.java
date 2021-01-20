package io.micronaut.starter.feature.config;

import edu.umd.cs.findbugs.annotations.NonNull;

public class BootstrapConfiguration extends Configuration {

    public BootstrapConfiguration(@NonNull String sourceSet, @NonNull String environment) {
        super(sourceSet, "bootstrap-" + environment, "bootstrap-config-" + environment);
    }

    public BootstrapConfiguration(@NonNull String environment) {
        this("main", environment);
    }

    public BootstrapConfiguration() {
        super("main", "bootstrap", "bootstrap-config");
    }

    public static BootstrapConfiguration testConfig() {
        return new BootstrapConfiguration("test", "test");
    }
}

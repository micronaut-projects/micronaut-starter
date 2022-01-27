package io.micronaut.starter.feature.build.gradle;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

public interface BuildRemoteCacheConfiguration {

    @NonNull
    String getUrl();

    @Nullable
    Credentials getCredentials();

    boolean push();
}

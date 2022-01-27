package io.micronaut.starter.feature.build.gradle;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

public interface Credentials {
    @NonNull
    String getUsername();
    @NonNull
    String getPassword();
}

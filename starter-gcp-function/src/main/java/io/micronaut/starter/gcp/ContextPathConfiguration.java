package io.micronaut.starter.gcp;


import io.micronaut.context.annotation.Primary;
import io.micronaut.http.context.ServerContextPathProvider;

import javax.annotation.Nullable;
import javax.inject.Singleton;

/**
 * Overrides the context path to below /api.
 */
@Singleton
@Primary
public class ContextPathConfiguration implements ServerContextPathProvider {
    @Nullable
    @Override
    public String getContextPath() {
        return "/api";
    }
}

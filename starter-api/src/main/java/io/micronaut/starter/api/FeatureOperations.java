package io.micronaut.starter.api;

import io.micronaut.http.annotation.Get;

import java.util.List;

/**
 * API to expose information about features.
 *
 * @author graemerocher
 * @since 1.0.0
 */
public interface FeatureOperations {
    /**
     * List all the available features.
     * @return The available features
     */
    @Get("/")
    List<FeatureDTO> features();
}

package io.micronaut.starter.api;

import io.micronaut.http.annotation.Get;

import java.util.List;

public interface FeatureOperations {
    @Get("/")
    List<FeatureDTO> features();
}

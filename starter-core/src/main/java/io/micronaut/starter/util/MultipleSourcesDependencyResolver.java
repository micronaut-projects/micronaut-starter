package io.micronaut.starter.util;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.projectgen.core.buildtools.dependencies.Coordinate;
import io.micronaut.projectgen.core.buildtools.dependencies.PomDependencyVersionResolver;
import io.micronaut.starter.build.dependencies.CoordinatesUtils;
import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Default implementation of {@link PomDependencyVersionResolver}.
 */
@Replaces(PomDependencyVersionResolver.class)
@Singleton
class MultipleSourcesDependencyResolver implements PomDependencyVersionResolver {
    private final Map<String, Coordinate> coordinates;

    MultipleSourcesDependencyResolver(ResourceResolver resourceResolver) {
        Map<String, Coordinate> allCoordinates = new HashMap<>();
        allCoordinates.putAll(io.micronaut.starter.build.dependencies.StarterCoordinates.ALL_COORDINATES);
        allCoordinates.putAll(io.micronaut.projectgen.build.dependencies.StarterCoordinates.ALL_COORDINATES);
        allCoordinates.putAll(CoordinatesUtils.readCoordinates(resourceResolver.getResources("classpath:pom.xml")));
        this.coordinates = Collections.unmodifiableMap(allCoordinates);
    }

    @Override
    public Optional<Coordinate> resolve(String artifactId) {
        return Optional.ofNullable(coordinates.get(artifactId));
    }

    @Override
    public Map<String, Coordinate> getCoordinates() {
        return coordinates;
    }
}

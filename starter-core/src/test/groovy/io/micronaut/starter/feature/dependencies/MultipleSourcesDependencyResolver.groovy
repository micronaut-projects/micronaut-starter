package io.micronaut.starter.feature.dependencies

import io.micronaut.context.annotation.Replaces
import io.micronaut.core.io.ResourceResolver
import io.micronaut.starter.build.dependencies.Coordinate
import io.micronaut.starter.build.dependencies.CoordinatesUtils
import io.micronaut.starter.build.dependencies.PomDependencyVersionResolver
import io.micronaut.starter.build.dependencies.StarterCoordinates
import jakarta.inject.Singleton

@Replaces(PomDependencyVersionResolver.class)
@Singleton
class MultipleSourcesDependencyResolver implements PomDependencyVersionResolver {
    private final Map<String, Coordinate> coordinates;

    MultipleSourcesDependencyResolver(ResourceResolver resourceResolver) {
        Map<String, Coordinate> allCoordinates = new HashMap<>();
        allCoordinates.putAll(StarterCoordinates.ALL_COORDINATES);
        allCoordinates.putAll(CoordinatesUtils.readCoordinates(resourceResolver.getResources("classpath:pom.xml")));
        this.coordinates = Collections.unmodifiableMap(allCoordinates);
    }
    @Override
    Optional<Coordinate> resolve(String artifactId) {
        return Optional.ofNullable(coordinates.get(artifactId));
    }

    @Override
    Map<String, Coordinate> getCoordinates() {
        return coordinates;
    }
}

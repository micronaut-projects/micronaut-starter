package io.micronaut.starter.feature.dependencies

import io.micronaut.context.annotation.Replaces
import io.micronaut.core.io.ResourceResolver
import io.micronaut.starter.build.dependencies.Coordinate
import io.micronaut.starter.build.dependencies.CoordinatesUtils
import io.micronaut.starter.build.dependencies.PomDependencyVersionResolver
import jakarta.inject.Singleton

@Replaces(PomDependencyVersionResolver.class)
@Singleton
class MultiplePomDependencyResolver implements PomDependencyVersionResolver {
    private final Map<String, Coordinate> coordinates;

    MultiplePomDependencyResolver(ResourceResolver resourceResolver) {
        this.coordinates = CoordinatesUtils.readCoordinates(resourceResolver.getResources("classpath:pom.xml"));
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

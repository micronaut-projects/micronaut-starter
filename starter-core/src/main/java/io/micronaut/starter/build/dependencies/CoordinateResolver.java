package io.micronaut.starter.build.dependencies;

import java.util.Optional;

public interface CoordinateResolver {

    Optional<Coordinate> resolve(String artifactId);
}

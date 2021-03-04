/*
 * Copyright 2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.build.maven;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.starter.build.Coordinate;
import io.micronaut.starter.build.dependencies.DependencyLookup;
import io.micronaut.starter.build.dependencies.DependencyVersionResolver;
import io.micronaut.starter.build.dependencies.PropertiesResolver;
import io.micronaut.starter.build.dependencies.ScopedDependency;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Optional;

@Singleton
public class MavenCoordinateResolver {
    public static final String IO_MICRONAUT = "io.micronaut";
    private final DependencyVersionResolver[] dependencyVersionResolvers;
    private final PropertiesResolver propertiesResolver;

    public MavenCoordinateResolver(DependencyVersionResolver[] dependencyVersionResolvers,
                                   PropertiesResolver propertiesResolver) {
        this.dependencyVersionResolvers = dependencyVersionResolvers;
        this.propertiesResolver = propertiesResolver;
    }

    @NonNull
    public Optional<Coordinate> coordinateByArtifactId(@NonNull String artifactId) {
        return Arrays.stream(dependencyVersionResolvers)
                .map(resolver -> resolver.findByArtifactId(artifactId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    @NonNull
    public Optional<ScopedDependency> resolve(@NonNull DependencyLookup lookup, boolean resolveVersionProperty) {
        Optional<Coordinate> mavenCoordinateOptional = coordinateByArtifactId(lookup.getArtifactId());
        if (mavenCoordinateOptional.isPresent()) {
            Coordinate coordinate = mavenCoordinateOptional.get();
            final String version = !coordinate.getGroupId().startsWith(IO_MICRONAUT) && resolveVersionProperty ?
                    resolveVersion(coordinate) :
                    coordinate.getVersion();
            return Optional.of(new ScopedDependency(lookup.getScope(), coordinate.getGroupId(), coordinate.getArtifactId(), version, lookup.getOrder()));
        }
        return Optional.empty();
    }

    @Nullable
    private String resolveVersion(@NonNull Coordinate coordinate) {
        Optional<String> kOptional = propertiesResolver.getPropertyKey(coordinate.getVersion());
        if (kOptional.isPresent()) {
            String k = kOptional.get();
            return propertiesResolver.resolve(k).orElse(null);
        }
        return coordinate.getVersion();
    }
}

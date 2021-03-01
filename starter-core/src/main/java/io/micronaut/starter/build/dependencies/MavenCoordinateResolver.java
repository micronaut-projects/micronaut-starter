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
package io.micronaut.starter.build.dependencies;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

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
    public Optional<MavenCoordinate> coordinateByArtifactId(@NonNull String artifactId) {
        return Arrays.stream(dependencyVersionResolvers)
                .map(resolver -> resolver.findByArtifactId(artifactId))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    @NonNull
    public Optional<ScopedDependency> resolve(@NonNull ScopedArtifact scopedArtifactId, boolean resolveVersionProperty) {
        Optional<MavenCoordinate> mavenCoordinateOptional = coordinateByArtifactId(scopedArtifactId.getArtifactId());
        if (mavenCoordinateOptional.isPresent()) {
            MavenCoordinate coordinate = mavenCoordinateOptional.get();
            final String version = !coordinate.getGroupId().startsWith(IO_MICRONAUT) && resolveVersionProperty ?
                    resolveVersion(coordinate) :
                    coordinate.getVersion();
            return Optional.of(new ScopedDependency() {
                @NonNull
                @Override
                public Scope getScope() {
                    return scopedArtifactId.getScope();
                }

                @NonNull
                @Override
                public String getGroupId() {
                    return coordinate.getGroupId();
                }

                @NonNull
                @Override
                public String getArtifactId() {
                    return coordinate.getArtifactId();
                }

                @Nullable
                @Override
                public String getVersion() {
                    return version;
                }
            });
        }
        return Optional.empty();
    }

    @Nullable
    private String resolveVersion(@NonNull MavenCoordinate coordinate) {
        Optional<String> kOptional = propertiesResolver.getPropertyKey(coordinate.getVersion());
        if (kOptional.isPresent()) {
            String k = kOptional.get();
            return propertiesResolver.resolve(k).orElse(null);
        }
        return coordinate.getVersion();
    }
}

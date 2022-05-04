/*
 * Copyright 2017-2022 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.starter.application.generator;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.DependencyContext;
import io.micronaut.starter.build.dependencies.LookupFailedException;

import java.util.HashSet;
import java.util.Set;

public class DependencyContextImpl implements DependencyContext {
    private final CoordinateResolver coordinateResolver;
    private final Set<Dependency> dependencies = new HashSet<>();

    public DependencyContextImpl(CoordinateResolver coordinateResolver) {
        this.coordinateResolver = coordinateResolver;
    }

    @Override
    public void addDependency(@NonNull Dependency dependency) {
        if (dependency.requiresLookup()) {
            Coordinate coordinate = coordinateResolver.resolve(dependency.getArtifactId())
                    .orElseThrow(() -> new LookupFailedException(dependency.getArtifactId()));
            this.dependencies.add(dependency.resolved(coordinate));
        } else {
            this.dependencies.add(dependency);
        }
    }

    @NonNull
    @Override
    public Set<Dependency> getDependencies() {
        return dependencies;
    }

    public Coordinate resolveCoordinate(String artifactId) {
        return coordinateResolver.resolve(artifactId)
                .orElseThrow(() -> new LookupFailedException(artifactId));
    }

}

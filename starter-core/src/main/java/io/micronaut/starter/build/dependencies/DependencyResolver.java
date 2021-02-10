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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.starter.options.BuildTool;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DependencyResolver implements BuildToolDependencyResolver {
    private final MavenCoordinateResolver mavenCoordinateResolver;
    private final AdapterBuilder adapterBuilder;
    private final BuildTool buildTool;
    private final Comparator<Dependency> comparator;

    public DependencyResolver(MavenCoordinateResolver mavenCoordinateResolver,
                              AdapterBuilder adapterBuilder,
                              BuildTool buildTool,
                              Comparator<Dependency> comparator) {
        this.mavenCoordinateResolver = mavenCoordinateResolver;
        this.adapterBuilder = adapterBuilder;
        this.buildTool = buildTool;
        this.comparator = comparator;
    }

    @Override
    @NonNull
    public List<Dependency> resolve(@NonNull Set<ScopedArtifact> artifacts) {
        return artifacts
                .stream()
                .map(mavenCoordinateResolver::resolve)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(dep -> adapterBuilder.build(dep, buildTool))
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}

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
import io.micronaut.starter.options.BuildTool;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DependencyResolver implements BuildToolDependencyResolver {
    protected final MavenCoordinateResolver mavenCoordinateResolver;
    protected final Comparator<MavenCoordinate> coordinateComparator = new MavenCoordinateComparator();
    protected final AdapterBuilder adapterBuilder;
    protected final BuildTool buildTool;
    protected final Comparator<Dependency> comparator;

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
                .map(scopedArtifact -> mavenCoordinateResolver.resolve(scopedArtifact, false))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(dep -> adapterBuilder.build(dep, buildTool))
                .filter(dep -> dep.getScope().isPresent())
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    public List<MavenCoordinate> annotationProcessors(@NonNull Set<ScopedArtifact> artifacts) {
        return artifacts
                .stream()
                .filter(dep -> dep.getScope().getPhases().contains(Phase.ANNOTATION_PROCESSING))
                .map(scopedArtifact -> mavenCoordinateResolver.resolve(scopedArtifact, false))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(coordinateComparator)
                .collect(Collectors.toList());
    }
}

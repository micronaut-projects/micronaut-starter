/*
 * Copyright 2017-2020 original authors
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
package io.micronaut.starter.build.dependencies;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.order.Ordered;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Introspected
public class DependencyCoordinate implements Coordinate, Ordered {

    private final String groupId;
    private final String artifactId;
    @Nullable
    private final String version;
    private final int order;
    private final boolean pom;

    @Nullable
    private final List<DependencyCoordinate> exclusions;

    @Nullable
    private final List<Substitution> substitutions;

    public DependencyCoordinate(Dependency dependency) {
        this(dependency, false);
    }

    public DependencyCoordinate(Dependency dependency, boolean showVersionProperty) {
        this(dependency.getGroupId(),
                dependency.getArtifactId(),
                showVersionProperty && dependency.getVersionProperty() != null ?
                    "${" + dependency.getVersionProperty() + "}" : dependency.getVersion(),
                dependency.getOrder(),
                dependency.isPom(),
                dependency.getExclusions() == null ? null :
                        dependency.getExclusions()
                                .stream()
                                .map(DependencyCoordinate::new)
                                .collect(Collectors.toList()),
                dependency.getSubstitutions());
    }

    public DependencyCoordinate(String groupId,
                                String artifactId,
                                @Nullable String version,
                                int order,
                                boolean pom) {
        this(groupId,
                artifactId,
                version,
                order,
                pom,
                null,
                null);
    }

    public DependencyCoordinate(String groupId,
                                String artifactId,
                                @Nullable String version,
                                int order,
                                boolean pom,
                                @Nullable List<DependencyCoordinate> exclusions,
                                @Nullable List<Substitution> substitutions) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.order = order;
        this.pom = pom;
        this.exclusions = exclusions;
        this.substitutions = substitutions;
    }

    @Nullable
    public List<Substitution> getSubstitutions() {
        return substitutions;
    }

    @Nullable
    public List<DependencyCoordinate> getExclusions() {
        return exclusions;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @NonNull
    @Override
    public String getGroupId() {
        return groupId;
    }

    @NonNull
    @Override
    public String getArtifactId() {
        return artifactId;
    }

    @Nullable
    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public boolean isPom() {
        return pom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DependencyCoordinate that = (DependencyCoordinate) o;

        return Objects.equals(getGroupId(), that.getGroupId()) &&
                Objects.equals(getArtifactId(), that.getArtifactId()) &&
                isPom() == that.isPom();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGroupId(), getArtifactId(), isPom());
    }
}

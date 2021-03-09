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
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.order.Ordered;

import java.util.Objects;

@Introspected
public class DependencyCoordinate implements Coordinate, Ordered {

    private final String groupId;
    private final String artifactId;
    @Nullable
    private final String version;
    private final int order;

    public DependencyCoordinate(Dependency dependency) {
        this(dependency, false);
    }

    public DependencyCoordinate(Dependency dependency, boolean showVersionProperty) {
        this.groupId = dependency.getGroupId();
        this.artifactId = dependency.getArtifactId();
        if (showVersionProperty && dependency.getVersionProperty() != null) {
            this.version = "${" + dependency.getVersionProperty() + "}";
        } else {
            this.version = dependency.getVersion();
        }
        this.order = dependency.getOrder();
    }

    public DependencyCoordinate(String groupId, String artifactId, @Nullable String version, int order) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.order = order;
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DependencyCoordinate that = (DependencyCoordinate) o;

        return Objects.equals(getGroupId(), that.getGroupId()) &&
                Objects.equals(getArtifactId(), that.getArtifactId());
    }

    @Override
    public int hashCode() {
        int result = getGroupId().hashCode();
        result = 31 * result + getArtifactId().hashCode();
        return result;
    }
}

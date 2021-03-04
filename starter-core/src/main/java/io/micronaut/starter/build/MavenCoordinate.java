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
package io.micronaut.starter.build;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.order.Ordered;

@Introspected
public class MavenCoordinate implements Coordinate, Ordered {
    @NonNull
    private String groupId;

    @NonNull
    private String artifactId;

    @Nullable
    private String version;

    private int order = 0;

    public MavenCoordinate(@NonNull String groupId, @NonNull String artifactId) {
        this(groupId, artifactId, null);
    }

    public MavenCoordinate(@NonNull String groupId, @NonNull String artifactId, @Nullable String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public MavenCoordinate(@NonNull String groupId, @NonNull String artifactId, @Nullable String version, int order) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @NonNull
    @Override
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(@NonNull String groupId) {
        this.groupId = groupId;
    }

    @NonNull
    @Override
    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(@NonNull String artifactId) {
        this.artifactId = artifactId;
    }

    @Nullable
    @Override
    public String getVersion() {
        return version;
    }

    public void setVersion(@Nullable String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MavenCoordinate that = (MavenCoordinate) o;

        if (!groupId.equals(that.groupId)) {
            return false;
        }
        if (!artifactId.equals(that.artifactId)) {
            return false;
        }
        return version != null ? version.equals(that.version) : that.version == null;
    }

    @Override
    public int hashCode() {
        int result = groupId.hashCode();
        result = 31 * result + artifactId.hashCode();
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }
}

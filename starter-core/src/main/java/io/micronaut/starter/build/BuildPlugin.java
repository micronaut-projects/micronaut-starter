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
import io.micronaut.core.order.Ordered;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.Writable;

import java.util.Objects;

public final class BuildPlugin implements Ordered {
    @NonNull
    private final BuildTool buildTool;

    @Nullable
    private final String id;

    @Nullable
    private final String groupId;

    @Nullable
    private final String artifactId;

    @Nullable
    private final String version;

    private final int order;

    private final boolean requiresLookup;

    @Nullable
    private final Writable extension;

    private BuildPlugin(@NonNull BuildTool buildTool,
                        @Nullable String groupId,
                        @Nullable String artifactId,
                        @Nullable String version,
                        @Nullable String id,
                        @Nullable Writable extension,
                        boolean requiresLookup,
                        int order) {
        this.buildTool = buildTool;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.id = id;
        this.extension = extension;
        this.requiresLookup = requiresLookup;
        this.order = order;
    }

    @Nullable
    public String getId() {
        return id;
    }

    @Nullable
    public String getVersion() {
        return version;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    @Nullable
    public Writable getExtension() {
        return extension;
    }

    @NonNull
    public BuildTool getBuildTool() {
        return buildTool;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public boolean requiresLookup() {
        return requiresLookup;
    }

    public BuildPlugin resolved(Coordinate coordinate) {
        return new BuildPlugin(buildTool, coordinate.getGroupId(), coordinate.getArtifactId(), coordinate.getVersion(), id, extension, requiresLookup, order);
    }

    public static BuildPlugin.Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BuildPlugin that = (BuildPlugin) o;
        return order == that.order && requiresLookup == that.requiresLookup && id.equals(that.id) && Objects.equals(groupId, that.groupId) && Objects.equals(artifactId, that.artifactId) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, artifactId, version, order, requiresLookup);
    }

    public static class Builder {
        @NonNull
        private BuildTool buildTool;

        @Nullable
        private String id;

        @Nullable
        private String groupId;

        @Nullable
        private String artifactId;

        @Nullable
        private String version;

        @Nullable
        private Writable extension;

        private boolean requiresLookup;
        private int order = 0;

        @NonNull
        public BuildPlugin.Builder tool(@NonNull BuildTool buildTool) {
            this.buildTool = buildTool;
            return this;
        }

        @NonNull
        public BuildPlugin.Builder id(@NonNull String id) {
            this.id = id;
            return this;
        }

        @NonNull
        public BuildPlugin.Builder groupId(@Nullable String groupId) {
            this.groupId = groupId;
            return this;
        }

        @NonNull
        public BuildPlugin.Builder artifactId(@Nullable String artifactId) {
            this.artifactId = artifactId;
            return this;
        }

        @NonNull
        public BuildPlugin.Builder lookupArtifactId(@NonNull String artifactId) {
            this.artifactId = artifactId;
            this.requiresLookup = true;
            return this;
        }

        @NonNull
        public BuildPlugin.Builder version(@Nullable String version) {
            this.version = version;
            return this;
        }

        @NonNull
        public BuildPlugin.Builder order(int order) {
            this.order = order;
            return this;
        }

        @NonNull
        public BuildPlugin.Builder extension(Writable extension) {
            this.extension = extension;
            return this;
        }

        @NonNull
        public BuildPlugin build() {
            Objects.requireNonNull(buildTool, "The buildTool must be set");
            return new BuildPlugin(buildTool, groupId, artifactId, version, id, extension, requiresLookup, order);
        }
    }
}

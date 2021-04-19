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
package io.micronaut.starter.build.gradle;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.build.BuildPlugin;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.dependencies.LookupFailedException;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.Writable;

import java.util.Objects;

public class GradlePlugin implements BuildPlugin {

    private final String id;
    private final String version;
    private final String artifactId;
    private final Writable extension;
    private final boolean requiresLookup;
    private final int order;

    public GradlePlugin(@NonNull String id,
                        @Nullable String version,
                        @Nullable String artifactId,
                        @Nullable Writable extension,
                        boolean requiresLookup,
                        int order) {
        this.id = id;
        this.version = version;
        this.artifactId = artifactId;
        this.extension = extension;
        this.requiresLookup = requiresLookup;
        this.order = order;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getVersion() {
        return version;
    }

    @Override
    @NonNull
    public BuildTool getBuildTool() {
        return null;
    }

    @Override
    @Nullable
    public Writable getExtension() {
        return extension;
    }

    @Override
    public boolean requiresLookup() {
        return requiresLookup;
    }

    @Override
    public BuildPlugin resolved(CoordinateResolver coordinateResolver) {
        Coordinate coordinate = coordinateResolver.resolve(artifactId)
                .orElseThrow(() -> new LookupFailedException(artifactId));
        return new GradlePlugin(id, coordinate.getVersion(), null, extension, false, order);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GradlePlugin that = (GradlePlugin) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String id;
        private String artifactId;
        private String version;
        private Writable extension;
        private boolean requiresLookup;
        private int order = 0;

        private Builder() { }

        @NonNull
        public GradlePlugin.Builder id(@NonNull String id) {
            this.id = id;
            return this;
        }

        @NonNull
        public GradlePlugin.Builder lookupArtifactId(@NonNull String artifactId) {
            this.artifactId = artifactId;
            this.requiresLookup = true;
            return this;
        }

        @NonNull
        public GradlePlugin.Builder version(@Nullable String version) {
            this.version = version;
            return this;
        }

        @NonNull
        public GradlePlugin.Builder extension(@Nullable Writable extension) {
            this.extension = extension;
            return this;
        }

        @NonNull
        public GradlePlugin.Builder order(int order) {
            this.order = order;
            return this;
        }

        public GradlePlugin build() {
            return new GradlePlugin(id, version, artifactId, extension, requiresLookup, order);
        }
    }

}

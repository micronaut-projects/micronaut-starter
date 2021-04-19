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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.build.BuildPlugin;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.Writable;

import java.util.Objects;

public class MavenPlugin implements BuildPlugin {

    private final Writable extension;
    private final int order;

    public MavenPlugin(Writable extension,
                       int order) {

        this.extension = extension;
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public boolean requiresLookup() {
        return false;
    }

    @Override
    public MavenPlugin resolved(CoordinateResolver coordinateResolver) {
        throw new UnsupportedOperationException();
    }

    public static MavenPlugin.Builder builder() {
        return new Builder();
    }

    @NonNull
    @Override
    public BuildTool getBuildTool() {
        return BuildTool.MAVEN;
    }

    @Override
    @Nullable
    public Writable getExtension() {
        return extension;
    }

    public static class Builder {

        private Writable extension;
        private int order = 0;

        private Builder() {
        }

        @NonNull
        public MavenPlugin.Builder extension(@Nullable Writable extension) {
            this.extension = extension;
            return this;
        }

        @NonNull
        public MavenPlugin.Builder order(int order) {
            this.order = order;
            return this;
        }

        @NonNull
        public MavenPlugin build() {
            Objects.requireNonNull(extension, "Maven plugins require an extension");
            return new MavenPlugin(extension, order);
        }
    }
}

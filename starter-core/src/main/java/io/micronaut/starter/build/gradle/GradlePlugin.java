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
package io.micronaut.starter.build.gradle;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.starter.build.BuildPlugin;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.dependencies.CoordinateResolver;
import io.micronaut.starter.build.dependencies.LookupFailedException;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.template.Writable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class GradlePlugin implements BuildPlugin {
    public static final int ORDER = -10;

    private final GradleFile gradleFile;
    private final String id;
    private final String version;
    private final String artifactId;
    private final Writable extension;
    private final Writable settingsExtension;
    private final boolean requiresLookup;
    private final List<GradleRepository> pluginsManagementRepositories;
    private final Set<String> buildImports;
    private final int order;

    public GradlePlugin(@NonNull GradleFile gradleFile,
                        @Nullable String id,
                        @Nullable String version,
                        @Nullable String artifactId,
                        @Nullable Writable extension,
                        @Nullable Writable settingsExtension,
                        List<GradleRepository> pluginsManagementRepositories,
                        boolean requiresLookup,
                        int order,
                        Set<String> buildImports) {
        this.gradleFile = gradleFile;
        this.id = id;
        this.version = version;
        this.artifactId = artifactId;
        this.extension = extension;
        this.settingsExtension = settingsExtension;
        this.pluginsManagementRepositories = pluginsManagementRepositories;
        this.requiresLookup = requiresLookup;
        this.order = order;
        this.buildImports = buildImports;
    }

    public static GradlePlugin of(String id, String lookupArtifactId) {
        return GradlePlugin.builder()
                .id(id)
                .lookupArtifactId(lookupArtifactId)
                .order(ORDER)
                .build();
    }

    @Nullable
    public Set<String> getBuildImports() {
        return buildImports;
    }

    @NonNull
    public GradleFile getGradleFile() {
        return gradleFile;
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
        return BuildTool.GRADLE;
    }

    @Override
    @Nullable
    public Writable getExtension() {
        return extension;
    }

    @Nullable
    public Writable getSettingsExtension() {
        return this.settingsExtension;
    }

    @NonNull
    public List<GradleRepository> getPluginsManagementRepositories() {
        return CollectionUtils.isEmpty(pluginsManagementRepositories) ?
                Collections.emptyList() : pluginsManagementRepositories;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public boolean requiresLookup() {
        return requiresLookup;
    }

    @Override
    public BuildPlugin resolved(CoordinateResolver coordinateResolver) {
        Coordinate coordinate = coordinateResolver.resolve(artifactId)
                .orElseThrow(() -> new LookupFailedException(artifactId));
        return new GradlePlugin(gradleFile, id, coordinate.getVersion(), null, extension, settingsExtension, pluginsManagementRepositories, false, order, buildImports);
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

    public static final class Builder {

        private GradleFile gradleFile = GradleFile.BUILD;
        private String id;
        private String artifactId;
        private String version;
        private Writable extension;
        private Writable settingsExtension;
        private List<GradleRepository> pluginsManagementRepositories;
        private boolean requiresLookup;
        private int order;
        private Set<String> buildImports = new HashSet<>();

        private Builder() { }

        @NonNull
        public GradlePlugin.Builder gradleFile(@NonNull GradleFile file) {
            this.gradleFile = file;
            return this;
        }

        @NonNull
        public GradlePlugin.Builder id(@NonNull String id) {
            this.id = id;
            return this;
        }

        @NonNull
        public GradlePlugin.Builder buildImports(String... imports) {
            this.buildImports.addAll(Arrays.asList(imports));
            return this;
        }

        @NonNull
        public GradlePlugin.Builder lookupArtifactId(@NonNull String artifactId) {
            this.artifactId = artifactId;
            this.requiresLookup = true;
            return this;
        }

        @NonNull
        public Optional<String> getArtifiactId()  {
            return Optional.ofNullable(artifactId);
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
        public GradlePlugin.Builder settingsExtension(@Nullable Writable settingsExtension) {
            this.settingsExtension = settingsExtension;
            return this;
        }

        @NonNull
        public GradlePlugin.Builder order(int order) {
            this.order = order;
            return this;
        }

        @NonNull
        public GradlePlugin.Builder pluginsManagementRepository(GradleRepository repo) {
            if (this.pluginsManagementRepositories == null) {
                this.pluginsManagementRepositories = new ArrayList<>();
            }
            this.pluginsManagementRepositories.add(repo);
            return this;
        }

        public GradlePlugin build() {
            return new GradlePlugin(gradleFile, id, version, artifactId, extension, settingsExtension, pluginsManagementRepositories, requiresLookup, order, buildImports);
        }
    }

}

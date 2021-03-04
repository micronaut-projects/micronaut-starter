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

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.starter.build.BuildPluginLookup;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class GradlePluginLookup implements BuildPluginLookup {
    @NonNull
    @NotBlank
    private String id;

    @NonNull
    @NotBlank
    private String artifactId;

    @Nullable
    private String groupId;

    public GradlePluginLookup(@NonNull String id, @NonNull String artifactId) {
        this.id = id;
        this.artifactId = artifactId;
    }

    public GradlePluginLookup(@NonNull String id, @NonNull String artifactId, @Nullable String groupId) {
        this.id = id;
        this.artifactId = artifactId;
        this.groupId = groupId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(@NonNull String artifactId) {
        this.artifactId = artifactId;
    }

    @Nullable
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(@Nullable String groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GradlePluginLookup that = (GradlePluginLookup) o;
        return id.equals(that.id) && artifactId.equals(that.artifactId) && Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, artifactId, groupId);
    }
}

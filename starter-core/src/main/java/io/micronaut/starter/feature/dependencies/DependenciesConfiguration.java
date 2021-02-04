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
package io.micronaut.starter.feature.dependencies;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.context.annotation.EachProperty;
import io.micronaut.context.annotation.Parameter;

@EachProperty("dependencies")
public class DependenciesConfiguration implements MavenCoordinate {
    private final String name;

    @NonNull
    private String groupId;

    @NonNull
    private String artifactId;

    @Nullable
    private String version;

    public DependenciesConfiguration(@Parameter String name) {
        this.name = name;
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
}

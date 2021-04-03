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

import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.starter.template.Writable;

public class MavenBuildPlugin {

    @Nullable
    private String artifactId;

    @Nullable
    private String groupId;

    @Nullable
    private String version;

    @Nullable
    private Writable extension;

    public MavenBuildPlugin(@Nullable String groupId,
                            @Nullable String artifactId,
                            @Nullable String version,
                            @Nullable Writable extension) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.extension = extension;
    }

    @Nullable
    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(@Nullable String artifactId) {
        this.artifactId = artifactId;
    }

    @Nullable
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(@Nullable String groupId) {
        this.groupId = groupId;
    }

    @Nullable
    public String getVersion() {
        return version;
    }

    public void setVersion(@Nullable String version) {
        this.version = version;
    }

    @Nullable
    public Writable getExtension() {
        return extension;
    }

    public void setExtension(@Nullable Writable extension) {
        this.extension = extension;
    }
}

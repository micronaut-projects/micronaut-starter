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

import io.micronaut.core.annotation.NonNull;

public class ScopedArtifact {

    @NonNull
    private Scope scope;

    @NonNull
    private String artifactId;

    public ScopedArtifact(@NonNull Scope scope, @NonNull String artifactId) {
        this.scope = scope;
        this.artifactId = artifactId;
    }

    @NonNull
    public Scope getScope() {
        return scope;
    }

    public void setScope(@NonNull Scope scope) {
        this.scope = scope;
    }

    @NonNull
    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(@NonNull String artifactId) {
        this.artifactId = artifactId;
    }
}

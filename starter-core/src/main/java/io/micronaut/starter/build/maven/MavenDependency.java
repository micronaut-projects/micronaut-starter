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

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.starter.build.MavenCoordinate;

public class MavenDependency extends MavenCoordinate {

    @NonNull
    private MavenScope mavenScope;

    public MavenDependency(@NonNull MavenScope scope, @NonNull String artifactId, @NonNull String groupId, @Nullable String version) {
        super(artifactId, groupId, version);
        this.mavenScope = scope;
    }

    public MavenDependency(@NonNull MavenScope scope, @NonNull String artifactId, @NonNull String groupId, @Nullable String version, int order) {
        super(artifactId, groupId, version, order);
        this.mavenScope = scope;

    }

    public MavenScope getMavenScope() {
        return this.mavenScope;
    }

    public void setMavenScope(@NonNull MavenScope mavenScope) {
        this.mavenScope = mavenScope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        MavenDependency that = (MavenDependency) o;

        return mavenScope == that.mavenScope;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + mavenScope.hashCode();
        return result;
    }
}

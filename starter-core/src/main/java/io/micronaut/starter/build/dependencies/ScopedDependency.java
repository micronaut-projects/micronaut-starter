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

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.starter.build.MavenCoordinate;

public class ScopedDependency extends MavenCoordinate {
    @NonNull
    private Scope scope;

    public ScopedDependency(@NonNull Scope scope, @NonNull String groupId, @NonNull String artifactId) {
        this(scope, groupId, artifactId, null, 0);
    }

    public ScopedDependency(@NonNull Scope scope, @NonNull String groupId, @NonNull String artifactId, @Nullable String version) {
        this(scope, groupId, artifactId, version, 0);
    }

    public ScopedDependency(@NonNull Scope scope, @NonNull String groupId, @NonNull String artifactId, @Nullable String version, int order) {
        super(groupId, artifactId, version, order);
        this.scope = scope;
    }

    @NonNull
    public Scope getScope() {
        return scope;
    }

    public void setScope(@NonNull Scope scope) {
        this.scope = scope;
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

        ScopedDependency that = (ScopedDependency) o;

        return scope.equals(that.scope);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + scope.hashCode();
        return result;
    }
}

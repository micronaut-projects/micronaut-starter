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
package io.micronaut.starter.feature.build.maven;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.starter.build.dependencies.Dependency;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Profile {

    @NonNull
    private final String id;

    @Nullable
    private Set<Property> activationProperties;

    @Nullable
    private Set<Dependency> dependencies;

    public Profile(@NonNull String id,
                   @Nullable Set<Property> activationProperties,
                   @Nullable Set<Dependency> dependencies) {
        this.id = id;
        this.activationProperties = activationProperties;
        this.dependencies = dependencies;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public Set<Property> getActivationProperties() {
        return activationProperties;
    }

    @Nullable
    public Set<Dependency> getDependencies() {
        return dependencies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Profile profile = (Profile) o;

        if (!id.equals(profile.id)) {
            return false;
        }
        if (activationProperties != null ? !activationProperties.equals(profile.activationProperties) : profile.activationProperties != null) {
            return false;
        }
        return dependencies != null ? dependencies.equals(profile.dependencies) : profile.dependencies == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (activationProperties != null ? activationProperties.hashCode() : 0);
        result = 31 * result + (dependencies != null ? dependencies.hashCode() : 0);
        return result;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void addActivationProperties(@Nullable Set<Property> activationProperties) {
        if (activationProperties != null) {
            if (this.activationProperties == null) {
                this.activationProperties = activationProperties;
            } else {
                this.activationProperties.addAll(activationProperties);
            }
        }
    }

    public void addDependencies(@Nullable Set<Dependency> dependencies) {
        if (dependencies != null) {
            if (this.dependencies == null) {
                this.dependencies = dependencies;
            } else {
                this.dependencies.addAll(dependencies);
            }
        }
    }

    public static class Builder {
        private String id;
        private Set<Dependency> dependencies;
        private Set<Property> activationProperties;

        @NonNull
        public Builder dependency(@NonNull Dependency dependency) {
            if (dependencies == null) {
                dependencies = new HashSet<>();
            }
            dependencies.add(dependency);
            return this;
        }

        @NonNull
        public Builder activationProperty(@NonNull Property property) {
            if (activationProperties == null) {
                activationProperties = new HashSet<>();
            }
            activationProperties.add(property);
            return this;
        }

        @NonNull
        public Builder id(@NonNull String id) {
            this.id = id;
            return this;
        }

        public Profile build() {
            return new Profile(Objects.requireNonNull(id), activationProperties, dependencies);
        }
    }
}

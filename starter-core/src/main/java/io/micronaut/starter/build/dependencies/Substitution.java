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
package io.micronaut.starter.build.dependencies;

import io.micronaut.core.annotation.NonNull;

import java.util.Objects;

public class Substitution {

    @NonNull
    private final Dependency target;

    @NonNull
    private final Dependency replacement;

    Substitution(@NonNull Dependency target,
                 @NonNull Dependency replacement) {
        this.target = target;
        this.replacement = replacement;
    }

    @NonNull
    public Dependency getTarget() {
        return target;
    }

    @NonNull
    public Dependency getReplacement() {
        return replacement;
    }

    @NonNull
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Dependency target;
        private Dependency replacement;

        @NonNull
        public Builder target(@NonNull Dependency target) {
            this.target = target;
            return this;
        }

        @NonNull
        public Builder replacement(@NonNull Dependency replacement) {
            this.replacement = replacement;
            return this;
        }

        @NonNull
        public Substitution build() {
            return new Substitution(Objects.requireNonNull(target),
                    Objects.requireNonNull(replacement));
        }
    }
}

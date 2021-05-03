/*
 * Copyright 2017-2020 original authors
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

import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.List;
import java.util.Objects;

public class Scope {

    @NonNull
    private Source source;

    @NonNull
    private List<Phase> phases;

    public Scope(@NonNull Source source, @NonNull List<Phase> phases) {
        this.source = source;
        this.phases = phases;
    }

    @NonNull
    public Source getSource() {
        return source;
    }

    public void setSource(@NonNull Source source) {
        this.source = source;
    }

    @NonNull
    public List<Phase> getPhases() {
        return phases;
    }

    public void setPhases(@NonNull List<Phase> phases) {
        this.phases = phases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Scope scope = (Scope) o;
        return source == scope.source && phases.equals(scope.phases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, phases);
    }
}

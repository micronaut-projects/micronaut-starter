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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Scope {
    public static final Scope ANNOTATION_PROCESSOR = new Scope(Source.MAIN, Collections.singletonList(Phase.ANNOTATION_PROCESSING));
    public static final Scope COMPILE = new Scope(Source.MAIN, Arrays.asList(Phase.COMPILATION, Phase.RUNTIME));
    public static final Scope DEVELOPMENT_ONLY = new Scope(Source.MAIN, Collections.singletonList(Phase.DEVELOPMENT));
    public static final Scope COMPILE_ONLY = new Scope(Source.MAIN, Collections.singletonList(Phase.COMPILATION));
    public static final Scope TEST_RESOURCES_SERVICE = new Scope(Source.MAIN, Collections.singletonList(Phase.TEST_RESOURCES_SERVICE));
    public static final Scope RUNTIME = new Scope(Source.MAIN, Collections.singletonList(Phase.RUNTIME));
    public static final Scope TEST = new Scope(Source.TEST, Arrays.asList(Phase.COMPILATION, Phase.RUNTIME));
    public static final Scope TEST_ANNOTATION_PROCESSOR = new Scope(Source.TEST, Collections.singletonList(Phase.ANNOTATION_PROCESSING));
    public static final Scope TEST_COMPILE_ONLY = new Scope(Source.TEST, Collections.singletonList(Phase.COMPILATION));
    public static final Scope TEST_RUNTIME = new Scope(Source.TEST, Collections.singletonList(Phase.RUNTIME));
    public static final Scope OPENREWRITE = new Scope(Source.MAIN, Collections.singletonList(Phase.OPENREWRITE));
    public static final Scope NATIVE_IMAGE_COMPILE_ONLY = new Scope(Source.MAIN, Collections.singletonList(Phase.NATIVE_IMAGE_COMPILATION));

    @NonNull
    private Source source;

    @NonNull
    private List<Phase> phases;

    public Scope(@NonNull Source source,
                 @NonNull List<Phase> phases) {
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

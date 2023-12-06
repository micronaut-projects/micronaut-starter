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
import io.micronaut.core.order.Ordered;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Scope implements Ordered {
    public static final Scope ANNOTATION_PROCESSOR = new Scope(Source.MAIN, Collections.singletonList(Phase.ANNOTATION_PROCESSING), 0);
    public static final Scope API = new Scope(Source.MAIN, Arrays.asList(Phase.COMPILATION, Phase.PUBLIC_API, Phase.RUNTIME), 1);
    public static final Scope COMPILE = new Scope(Source.MAIN, Arrays.asList(Phase.COMPILATION, Phase.RUNTIME), 2);
    public static final Scope COMPILE_ONLY = new Scope(Source.MAIN, Collections.singletonList(Phase.COMPILATION), 3);
    public static final Scope RUNTIME = new Scope(Source.MAIN, Collections.singletonList(Phase.RUNTIME), 4);
    public static final Scope DEVELOPMENT_ONLY = new Scope(Source.MAIN, Collections.singletonList(Phase.DEVELOPMENT), 5);

    public static final Scope AOT_PLUGIN = new Scope(Source.MAIN, Collections.singletonList(Phase.AOT_PLUGIN), 6);
    public static final Scope OPENREWRITE = new Scope(Source.MAIN, Collections.singletonList(Phase.OPENREWRITE), 7);
    public static final Scope NATIVE_IMAGE_COMPILE_ONLY = new Scope(Source.MAIN, Collections.singletonList(Phase.NATIVE_IMAGE_COMPILATION), 8);

    public static final Scope TEST = new Scope(Source.TEST, Arrays.asList(Phase.COMPILATION, Phase.RUNTIME), 9);
    public static final Scope TEST_ANNOTATION_PROCESSOR = new Scope(Source.TEST, Collections.singletonList(Phase.ANNOTATION_PROCESSING), 10);
    public static final Scope TEST_COMPILE_ONLY = new Scope(Source.TEST, Collections.singletonList(Phase.COMPILATION), 11);
    public static final Scope TEST_RUNTIME = new Scope(Source.TEST, Collections.singletonList(Phase.RUNTIME), 12);

    public static final Scope TEST_RESOURCES_SERVICE = new Scope(Source.MAIN, Collections.singletonList(Phase.TEST_RESOURCES_SERVICE), 13);

    @NonNull
    private Source source;

    @NonNull
    private List<Phase> phases;

    private final Integer order;

    public Scope(@NonNull Source source,
                 @NonNull List<Phase> phases,
                 int order) {
        this.source = source;
        this.phases = phases;
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
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

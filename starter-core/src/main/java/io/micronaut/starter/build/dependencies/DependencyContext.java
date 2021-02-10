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
import io.micronaut.starter.options.TestFramework;

import java.util.Arrays;
import java.util.Collections;

public interface DependencyContext {

    @NonNull
    TestFramework getTestFramework();

    void addDependency(@NonNull ScopedArtifact scopedArtifact);

    default void addDependency(@NonNull String artifactId) {
        addDependency(new ScopedArtifact(new Scope(Source.MAIN, Arrays.asList(Phase.COMPILATION, Phase.RUNTIME)), artifactId));
    }

    default void addCompileOnlyDependency(@NonNull String artifactId) {
        addDependency(new ScopedArtifact(new Scope(Source.MAIN, Collections.singletonList(Phase.COMPILATION)), artifactId));
    }

    default void addRuntimeDependency(@NonNull String artifactId) {
        addDependency(new ScopedArtifact(new Scope(Source.MAIN, Collections.singletonList(Phase.RUNTIME)), artifactId));
    }

    default void addTestDependency(@NonNull String artifactId) {
        addDependency(new ScopedArtifact(new Scope(Source.TEST, Arrays.asList(Phase.COMPILATION, Phase.RUNTIME)), artifactId));
    }

    default void addTestCompileOnlyDependency(@NonNull String artifactId) {
        addDependency(new ScopedArtifact(new Scope(Source.TEST, Collections.singletonList(Phase.COMPILATION)), artifactId));
    }

    default void addTestRuntimeDependency(@NonNull String artifactId) {
        addDependency(new ScopedArtifact(new Scope(Source.TEST, Collections.singletonList(Phase.RUNTIME)), artifactId));
    }
}

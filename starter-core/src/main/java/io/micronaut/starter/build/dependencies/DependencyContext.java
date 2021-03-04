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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public interface DependencyContext {

    @NonNull
    Collection<DependencyLookup> getDependencyLookups();

    @NonNull
    Collection<ScopedDependency> getDependencies();

    void addDependencyLookup(@NonNull DependencyLookup dependencyLookup);

    void addDependency(@NonNull ScopedDependency dependency);

    default void addDependencyLookup(@NonNull String artifactId) {
        addDependencyLookup(new DependencyLookup(new Scope(Source.MAIN, Arrays.asList(Phase.COMPILATION, Phase.RUNTIME)), artifactId));
    }

    default void addDependency(@NonNull String groupId, @NonNull String artifactId) {
        addDependency(groupId, artifactId, null);
    }

    default void addDependency(@NonNull String groupId, @NonNull String artifactId, @Nullable String version) {
        addScopedDependency(new Scope(Source.MAIN, Arrays.asList(Phase.COMPILATION, Phase.RUNTIME)), groupId, artifactId, version);
    }

    default void addScopedDependency(@NonNull Scope scope, @NonNull String groupId, @NonNull String artifactId, @Nullable String version) {
        addDependency(new ScopedDependency(scope, groupId, artifactId, version));
    }

    default void addCompileOnlyDependencyLookup(@NonNull String artifactId) {
        addDependencyLookup(new DependencyLookup(new Scope(Source.MAIN, Collections.singletonList(Phase.COMPILATION)), artifactId));
    }

    default void addCompileOnlyDependency(@NonNull String groupId, @NonNull String artifactId) {
        addScopedDependency(new Scope(Source.MAIN, Collections.singletonList(Phase.COMPILATION)), groupId, artifactId, null);
    }

    default void addRuntimeDependencyLookup(@NonNull String artifactId) {
        addDependencyLookup(new DependencyLookup(new Scope(Source.MAIN, Collections.singletonList(Phase.RUNTIME)), artifactId));
    }

    default void addRuntimeDependency(@NonNull String groupId, @NonNull String artifactId) {
        addRuntimeDependency(groupId, artifactId, null);
    }

    default void addRuntimeDependency(@NonNull String groupId, @NonNull String artifactId, @Nullable String version) {
        addScopedDependency(new Scope(Source.MAIN, Collections.singletonList(Phase.RUNTIME)), groupId, artifactId, version);
    }

    default void addTestDependencyLookup(@NonNull String artifactId) {
        addDependencyLookup(new DependencyLookup(new Scope(Source.TEST, Arrays.asList(Phase.COMPILATION, Phase.RUNTIME)), artifactId));
    }

    default void addTestCompileOnlyDependencyLookup(@NonNull String artifactId) {
        addDependencyLookup(new DependencyLookup(new Scope(Source.TEST, Collections.singletonList(Phase.COMPILATION)), artifactId));
    }

    default void addTestRuntimeDependencyLookup(@NonNull String artifactId) {
        addDependencyLookup(new DependencyLookup(new Scope(Source.TEST, Collections.singletonList(Phase.RUNTIME)), artifactId));
    }

    default void addAnnotationProcessorLookup(@NonNull String artifactId) {
        addDependencyLookup(new DependencyLookup(new Scope(Source.MAIN, Collections.singletonList(Phase.ANNOTATION_PROCESSING)), artifactId));
    }

    default void addAnnotationProcessorLookup(@NonNull String artifactId, int order) {
        addDependencyLookup(new DependencyLookup(new Scope(Source.MAIN, Collections.singletonList(Phase.ANNOTATION_PROCESSING)), artifactId, order));
    }

    default void addAnnotationProcessor(@NonNull String artifactId, @NonNull String groupId, @NonNull String version) {
        addAnnotationProcessor(artifactId, groupId, version, 0);
    }

    default void addAnnotationProcessor(@NonNull String artifactId, @NonNull String groupId, @NonNull String version, int order) {
        addDependency(new ScopedDependency(new Scope(Source.MAIN, Collections.singletonList(Phase.ANNOTATION_PROCESSING)), artifactId, groupId, version, order));
    }

    default void addTestAnnotationProcessor(@NonNull String artifactId, @NonNull String groupId, @NonNull String version) {
        addTestAnnotationProcessor(artifactId, groupId, version, 0);
    }

    default void addTestAnnotationProcessor(@NonNull String artifactId, @NonNull String groupId, @NonNull String version, int order) {
        addDependency(new ScopedDependency(new Scope(Source.TEST, Collections.singletonList(Phase.ANNOTATION_PROCESSING)), artifactId, groupId, version, order));
    }
}

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
package io.micronaut.starter.feature.dependencies;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.starter.feature.Feature;
import io.micronaut.starter.feature.Features;
import io.micronaut.starter.feature.test.TestFeature;
import io.micronaut.starter.options.BuildTool;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface DependenciesFeature {

    default List<GradleDependency> getGradleDependencies(@Nullable TestFeature testFeature) {
        return Collections.emptyList();
    }

    default List<ScopedMavenCoordinate> getDependencies(@Nullable TestFeature testFeature,
                                                        @NonNull BuildTool buildTool) {
        return getGradleDependencies(testFeature).stream()
                .map(gradleDependency -> new ScopedMavenCoordinate(ScopeResolver.resolve(gradleDependency.getGradleConfiguration(), buildTool), gradleDependency.getMavenCoordinate()))
                .collect(Collectors.toList());
    }

    static Set<ScopedMavenCoordinate> collectCoordinates(@NonNull Features features,
                                                         @NonNull BuildTool buildTool) {
        Set<ScopedMavenCoordinate> result = new HashSet<>();
        for (Feature feature : features.getFeatures()) {
            if (feature instanceof DependenciesFeature) {
                result.addAll(((DependenciesFeature) feature).getDependencies(features.testFramework(), buildTool));
            }
        }
        return result;
    }
}

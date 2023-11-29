/*
 * Copyright 2017-2023 original authors
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
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static io.micronaut.starter.build.dependencies.Phase.COMPILATION;
import static io.micronaut.starter.build.dependencies.Phase.RUNTIME;

public interface DependencyContext {

    Predicate<Dependency> IS_COMPILE_API_OR_RUNTIME = d -> d.getScope().getPhases().contains(COMPILATION) ||
            d.getScope().getPhases().contains(Phase.PUBLIC_API) ||
            d.getScope().getPhases().contains(Phase.RUNTIME);
    
    @NonNull
    Collection<Dependency> getDependencies();

    void addDependency(@NonNull Dependency dependency);

    default void addDependency(@NonNull Dependency.Builder dependency) {
        addDependency(dependency.build());
    }

    @NonNull
    default List<Dependency> removeDuplicates(Collection<Dependency> dependencies, Language language, BuildTool buildTool) {

        List<Dependency> dependenciesNotInMainOrTestClasspath = dependencies.stream()
                .filter(d -> {
                    if (language == Language.GROOVY && buildTool == BuildTool.MAVEN) {
                        return !IS_COMPILE_API_OR_RUNTIME.test(d) && !d.getScope().getPhases().contains(Phase.ANNOTATION_PROCESSING);
                    }
                    return !IS_COMPILE_API_OR_RUNTIME.test(d);
                })
                .toList();

        List<Dependency> dependenciesInMainClasspath = dependencies.stream()
                .filter(d -> {
                    if (d.getScope().getSource() != Source.MAIN) {
                        return false;
                    }
                    if (language == Language.GROOVY && buildTool == BuildTool.MAVEN) {
                        return IS_COMPILE_API_OR_RUNTIME.test(d) || d.getScope().getPhases().contains(Phase.ANNOTATION_PROCESSING);
                    }
                    return IS_COMPILE_API_OR_RUNTIME.test(d);

                })
                .toList();
        List<Dependency> dependenciesInMainClasspathWithoutDuplicates = filterDuplicates(dependenciesInMainClasspath);

        List<Dependency> dependenciesInTestClasspath = dependencies.stream()
                .filter(d -> d.getScope().getSource() == Source.TEST && IS_COMPILE_API_OR_RUNTIME.test(d))
                .toList();

        List<Dependency> dependenciesInTestClasspathWithoutDuplicates = filterDuplicates(dependenciesInTestClasspath);

        dependenciesInTestClasspathWithoutDuplicates.removeIf(testDep -> {
            MavenCoordinate test = new MavenCoordinate(testDep.getGroupId(), testDep.getArtifactId(), testDep.getVersion());
            return dependenciesInMainClasspathWithoutDuplicates.stream()
                    .filter(mainDep -> (mainDep.getScope().getPhases().contains(RUNTIME) && mainDep.getScope().getPhases().contains(COMPILATION)))
                    .anyMatch(mainDep -> {
                        MavenCoordinate main = new MavenCoordinate(mainDep.getGroupId(), mainDep.getArtifactId(), mainDep.getVersion());
                        return main.equals(test);
                    });
        });
        List<Dependency> result = new ArrayList<>(dependenciesNotInMainOrTestClasspath);
        result.addAll(dependenciesInMainClasspathWithoutDuplicates);
        result.addAll(dependenciesInTestClasspathWithoutDuplicates);
        return result.stream().sorted(Dependency.COMPARATOR).toList();
    }

    private static List<Dependency> filterDuplicates(List<Dependency> dependencies) {
        return new ArrayList<>(dependencies.stream().collect(
                Collectors.toMap(
                        d -> new MavenCoordinate(d.getGroupId(), d.getArtifactId(), d.getVersion()),
                        Function.identity(),
                        BinaryOperator.minBy(Comparator.comparing(d -> d.getScope().getOrder()))
                )
        ).values());
    }
}

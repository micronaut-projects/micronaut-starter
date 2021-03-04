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
package io.micronaut.starter.build.maven;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.build.Coordinate;
import io.micronaut.starter.build.MavenCoordinate;
import io.micronaut.starter.build.dependencies.Phase;
import io.micronaut.starter.build.dependencies.PropertiesResolver;
import io.micronaut.starter.build.dependencies.ScopedDependency;
import io.micronaut.starter.build.dependencies.Source;
import io.micronaut.starter.options.Language;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class MavenBuildCreator {
    private static final Function<ScopedDependency, Optional<MavenDependency>> TO_MAVEN_DEPENDENCY = dep -> {
        Optional<MavenScope> mavenScopeOptional = MavenScope.of(dep.getScope());
        return mavenScopeOptional.map(mavenScope ->
                new MavenDependency(mavenScope, dep.getGroupId(), dep.getArtifactId(), dep.getVersion()));
    };
    public static final String MAVEN_GROUP_IO_MICRONAUT = "io.micronaut";

    private final PropertiesResolver propertiesResolver;
    private final MavenCoordinateResolver mavenCoordinateResolver;
    private final Comparator<MavenDependency> mavenDependencyComparator = new MavenDependencyComparator();
    private final Comparator<MavenCoordinate> mavenCoordinateComparator = new MavenCoordinateComparator();

    public MavenBuildCreator(MavenCoordinateResolver mavenCoordinateResolver,
                             PropertiesResolver propertiesResolver) {
        this.mavenCoordinateResolver = mavenCoordinateResolver;
        this.propertiesResolver = propertiesResolver;
    }

    @NonNull
    public MavenBuild create(GeneratorContext generatorContext) {
        List<MavenDependency> dependencies = resolveDependencies(generatorContext);
        BuildProperties buildProperties = generatorContext.getBuildProperties();
        List<MavenCoordinate> annotationProcessorsCoordinates =  annotationProcessorsBySource(generatorContext, Source.MAIN);
        MavenCombineAttribute annotationProcessorCombineAttribute = calculateCombineAttribute(annotationProcessorsCoordinates);
        List<MavenCoordinate> testAnnotationProcessorsCoordinates =  annotationProcessorsBySource(generatorContext, Source.TEST);
        MavenCombineAttribute testAnnotationProcessorCombineAttribute = calculateCombineAttribute(testAnnotationProcessorsCoordinates);
        return new MavenBuild(annotationProcessorsCoordinates,
                testAnnotationProcessorsCoordinates,
                dependencies,
                buildProperties.getProperties(),
                annotationProcessorCombineAttribute,
                testAnnotationProcessorCombineAttribute);
    }

    private MavenCombineAttribute calculateCombineAttribute(@NonNull List<MavenCoordinate> coordinates) {
        return coordinates.stream().anyMatch(coord -> !coord.getGroupId().startsWith("io.micronaut")) ?
                MavenCombineAttribute.SELF_OVERRIDE :
                MavenCombineAttribute.CHILDREN_APPEND;
    }

    @NonNull
    private List<MavenDependency> resolveDependencies(@NonNull GeneratorContext generatorContext) {

        List<MavenDependency> dependencies = mavenDependencies(generatorContext.getDependencies().stream());

        dependencies.addAll(mavenDependencies(generatorContext.getDependencyLookups()
                .stream()
                .map(lookup -> mavenCoordinateResolver.resolve(lookup, false))
                .filter(Optional::isPresent)
                .map(Optional::get)));

        dependencies.sort(mavenDependencyComparator);

        return dependencies;
    }

    private List<MavenDependency> mavenDependencies(Stream<ScopedDependency> dependencies) {
        return dependencies
                .map(TO_MAVEN_DEPENDENCY)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @NonNull
    private Map<String, String> buildProperties(@NonNull List<? extends Coordinate> coordinates) {
        Map<String, String> result = new HashMap<>();
        for (Coordinate coordinate : coordinates) {
            Optional<String> kOptional = propertiesResolver.getPropertyKey(coordinate.getVersion());
            if (kOptional.isPresent()) {
                String k = kOptional.get();
                propertiesResolver.resolve(k).ifPresent(val -> result.put(k, val));
            }
        }
        return result;
    }

    @NonNull
    private List<MavenCoordinate> annotationProcessorsBySource(@NonNull GeneratorContext generatorContext, @NonNull Source source) {
        Set<MavenCoordinate> coordinates = generatorContext.getDependencies().stream()
                .filter(dep -> dep.getScope().getSource() == source && dep.getScope().getPhases().contains(Phase.ANNOTATION_PROCESSING))
                .collect(Collectors.toSet());
        coordinates.addAll(generatorContext.getDependencyLookups()
                .stream()
                .filter(dep -> dep.getScope().getSource() == source && dep.getScope().getPhases().contains(Phase.ANNOTATION_PROCESSING))
                .map(lookup -> mavenCoordinateResolver.resolve(lookup, false))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet()));
        if (coordinates.stream().anyMatch(coord -> !coord.getGroupId().startsWith("io.micronaut")) ||
            generatorContext.getLanguage() == Language.KOTLIN) {
            coordinates.add(new MavenCoordinate("io.micronaut", "micronaut-inject-java", "${micronaut.version}"));
            coordinates.add(new MavenCoordinate("io.micronaut", "micronaut-validation", "${micronaut.version}"));
        }
        List<MavenCoordinate> coordinateList = new ArrayList<>(coordinates);
        coordinateList.sort(mavenCoordinateComparator);
        return coordinateList;
    }
}

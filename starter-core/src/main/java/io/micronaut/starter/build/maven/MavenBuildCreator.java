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
package io.micronaut.starter.build.maven;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.order.OrderUtil;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.DependencyCoordinate;
import io.micronaut.starter.build.dependencies.Phase;
import io.micronaut.starter.build.dependencies.Source;
import io.micronaut.starter.options.Language;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class MavenBuildCreator {

    private static final String MICRONAUT_INJECT_JAVA = "micronaut-inject-java";
    private static final String MICRONAUT_INJECT = "micronaut-inject";
    private static final String MICRONAUT_DATA_PROCESSOR = "micronaut-data-processor";

    /**
     * For Maven annotation processing, the dependencies must be in a specific order, as in Maven, the first wins no matter what.
     *
     * This comparator ensures that the order is as follows (if they exist):
     *
     * - lombok
     * - micronaut-inject
     * - micronaut-inject-java
     * - micronaut-data-processor
     * - other dependencies
     */
    private static final Comparator<? super Coordinate> PROCESSOR_COMPARATOR = (o1, o2) -> {
        if ("lombok".equals(o1.getArtifactId()) && "org.projectlombok".equals(o1.getGroupId())) {
            return Integer.MIN_VALUE;
        } else if ("lombok".equals(o2.getArtifactId()) && "org.projectlombok".equals(o2.getGroupId())) {
            return Integer.MAX_VALUE;
        } else if (MICRONAUT_INJECT.equals(o1.getArtifactId())) {
            return Integer.MIN_VALUE + 1;
        } else if (MICRONAUT_INJECT.equals(o2.getArtifactId())) {
            return Integer.MAX_VALUE - 1;
        } else if (MICRONAUT_INJECT_JAVA.equals(o1.getArtifactId())) {
            return Integer.MIN_VALUE + 2;
        } else if (MICRONAUT_INJECT_JAVA.equals(o2.getArtifactId())) {
            return Integer.MAX_VALUE - 2;
        } else if (MICRONAUT_DATA_PROCESSOR.equals(o1.getArtifactId())) {
            return Integer.MIN_VALUE + 3;
        } else if (MICRONAUT_DATA_PROCESSOR.equals(o2.getArtifactId())) {
            return Integer.MAX_VALUE - 3;
        } else {
            return Coordinate.COMPARATOR.compare(o1, o2);
        }
    };

    @NonNull
    public MavenBuild create(GeneratorContext generatorContext) {
        List<MavenDependency> dependencies = resolveDependencies(generatorContext);
        BuildProperties buildProperties = generatorContext.getBuildProperties();
        List<Coordinate> annotationProcessorsCoordinates = new ArrayList<>();
        List<Coordinate> testAnnotationProcessorsCoordinates = new ArrayList<>();
        boolean isKotlin = generatorContext.getLanguage() == Language.KOTLIN;
        MavenCombineAttribute combineAttribute = isKotlin ? MavenCombineAttribute.OVERRIDE : MavenCombineAttribute.APPEND;
        MavenCombineAttribute testCombineAttribute = combineAttribute;

        for (Dependency dependency : generatorContext.getDependencies()) {
            if (dependency.getScope().getPhases().contains(Phase.ANNOTATION_PROCESSING)) {
                if (dependency.getScope().getSource() == Source.MAIN) {
                    annotationProcessorsCoordinates.add(new DependencyCoordinate(dependency, true));
                    if (dependency.isAnnotationProcessorPriority()) {
                        combineAttribute = MavenCombineAttribute.OVERRIDE;
                    }
                }
                if (dependency.getScope().getSource() == Source.TEST) {
                    testAnnotationProcessorsCoordinates.add(new DependencyCoordinate(dependency, true));
                    if (dependency.isAnnotationProcessorPriority()) {
                        testCombineAttribute = MavenCombineAttribute.OVERRIDE;
                    }
                }
            }
        }

        Coordinate injectJava = Dependency.builder()
                .groupId("io.micronaut")
                .artifactId(MICRONAUT_INJECT_JAVA)
                .versionProperty("micronaut.version")
                .buildCoordinate(true);
        Coordinate validation = Dependency.builder()
                .groupId("io.micronaut")
                .artifactId("micronaut-validation")
                .versionProperty("micronaut.version")
                .buildCoordinate(true);
        Coordinate mnGraal = Dependency.builder()
                .groupId("io.micronaut")
                .artifactId("micronaut-graal")
                .versionProperty("micronaut.version")
                .buildCoordinate(true);

        if (combineAttribute == MavenCombineAttribute.OVERRIDE) {
            annotationProcessorsCoordinates.add(injectJava);
            annotationProcessorsCoordinates.add(validation);
            annotationProcessorsCoordinates.add(mnGraal);
        }
        if (testCombineAttribute == MavenCombineAttribute.OVERRIDE) {
            testAnnotationProcessorsCoordinates.add(injectJava);
            testAnnotationProcessorsCoordinates.add(validation);
            annotationProcessorsCoordinates.add(mnGraal);
        }

        annotationProcessorsCoordinates.sort(PROCESSOR_COMPARATOR);
        testAnnotationProcessorsCoordinates.sort(PROCESSOR_COMPARATOR);

        List<MavenPlugin> plugins = generatorContext.getBuildPlugins()
                .stream()
                .filter(MavenPlugin.class::isInstance)
                .map(MavenPlugin.class::cast)
                .sorted(OrderUtil.COMPARATOR)
                .collect(Collectors.toList());

        return new MavenBuild(annotationProcessorsCoordinates,
                testAnnotationProcessorsCoordinates,
                dependencies,
                buildProperties.getProperties(),
                plugins,
                combineAttribute,
                testCombineAttribute);
    }

    @NonNull
    private List<MavenDependency> resolveDependencies(@NonNull GeneratorContext generatorContext) {
        return generatorContext.getDependencies()
                .stream()
                .filter(dep -> !dep.getScope().getPhases().contains(Phase.ANNOTATION_PROCESSING))
                .map(MavenDependency::new)
                .sorted(MavenDependency.COMPARATOR)
                .collect(Collectors.toList());
    }

}

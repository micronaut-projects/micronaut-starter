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
package io.micronaut.starter.build.maven;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.order.OrderUtil;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.BuildProperties;
import io.micronaut.starter.build.MavenCentral;
import io.micronaut.starter.build.Repository;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.DependencyCoordinate;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.build.dependencies.Phase;
import io.micronaut.starter.build.dependencies.Priority;
import io.micronaut.starter.build.dependencies.Source;
import io.micronaut.starter.options.Language;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class MavenBuildCreator {

    /**
     * Not used anymore
     *
     * @param generatorContext Generator Context
     * @return a Maven Build
     */
    @Deprecated
    @NonNull
    public MavenBuild create(GeneratorContext generatorContext) {
        return create(generatorContext, Collections.singletonList(new MavenCentral()));
    }

    @NonNull
    public MavenBuild create(GeneratorContext generatorContext, List<Repository> repositories) {
        List<MavenDependency> dependencies = MavenDependency.listOf(generatorContext);
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

        Coordinate injectJava = MicronautDependencyUtils.coreDependency()
                .artifactId("micronaut-inject-java")
                .versionProperty("micronaut.version")
                .order(Priority.MICRONAUT_INJECT_JAVA.getOrder())
                .buildCoordinate(true);
        Coordinate validation = MicronautDependencyUtils.coreDependency()
                .artifactId("micronaut-validation")
                .versionProperty("micronaut.version")
                .buildCoordinate(true);
        Coordinate mnGraal = MicronautDependencyUtils.coreDependency()
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
            testAnnotationProcessorsCoordinates.add(mnGraal);
        }

        annotationProcessorsCoordinates.sort(Coordinate.COMPARATOR);
        testAnnotationProcessorsCoordinates.sort(Coordinate.COMPARATOR);

        List<MavenPlugin> plugins = generatorContext.getBuildPlugins()
                .stream()
                .filter(MavenPlugin.class::isInstance)
                .map(MavenPlugin.class::cast)
                .sorted(OrderUtil.COMPARATOR)
                .collect(Collectors.toList());

        return new MavenBuild(generatorContext.getProject().getName(),
                annotationProcessorsCoordinates,
                testAnnotationProcessorsCoordinates,
                dependencies,
                buildProperties.getProperties(),
                plugins,
                MavenRepository.listOf(repositories),
                combineAttribute,
                testCombineAttribute,
                generatorContext.getProfiles());
    }

    /**
     * @deprecated Not used anymore
     * @return Empty list
     */
    @Deprecated
    @NonNull
    protected List<MavenRepository> getRepositories() {
        return Collections.emptyList();
    }
}

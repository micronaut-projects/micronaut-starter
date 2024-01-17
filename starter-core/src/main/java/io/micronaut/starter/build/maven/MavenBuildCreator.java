/*
 * Copyright 2017-2024 original authors
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
import io.micronaut.starter.build.Repository;
import io.micronaut.starter.build.dependencies.Coordinate;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.DependencyCoordinate;
import io.micronaut.starter.build.dependencies.MavenCoordinate;
import io.micronaut.starter.build.dependencies.MicronautDependencyUtils;
import io.micronaut.starter.build.dependencies.Phase;
import io.micronaut.starter.build.dependencies.Priority;
import io.micronaut.starter.build.dependencies.Scope;
import io.micronaut.starter.build.dependencies.Source;
import io.micronaut.starter.feature.testresources.TestResourcesAdditionalModulesProvider;
import io.micronaut.starter.options.Language;
import io.micronaut.starter.options.Options;
import jakarta.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class MavenBuildCreator {

    public static final String PROPERTY_MICRONAUT_CORE_VERSION = "micronaut.core.version";

    @NonNull
    public MavenBuild create(GeneratorContext generatorContext, List<Repository> repositories) {
        List<MavenDependency> dependencies = MavenDependency.listOf(generatorContext, generatorContext.getLanguage());
        BuildProperties buildProperties = generatorContext.getBuildProperties();
        List<DependencyCoordinate> annotationProcessorsCoordinates = new ArrayList<>();
        List<DependencyCoordinate> testAnnotationProcessorsCoordinates = new ArrayList<>();
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


        if (combineAttribute == MavenCombineAttribute.OVERRIDE) {
            DependencyCoordinate injectJava = MicronautDependencyUtils.injectJava()
                    .versionProperty(PROPERTY_MICRONAUT_CORE_VERSION)
                    .order(Priority.MICRONAUT_INJECT_JAVA.getOrder())
                    .buildCoordinate(true);
            annotationProcessorsCoordinates.add(injectJava);
            testAnnotationProcessorsCoordinates.add(injectJava);
            if (generatorContext.getFramework().equalsIgnoreCase(Options.FRAMEWORK_MICRONAUT)) {
                DependencyCoordinate mnGraal = MicronautDependencyUtils.coreDependency()
                        .artifactId("micronaut-graal")
                        .versionProperty(PROPERTY_MICRONAUT_CORE_VERSION)
                        .buildCoordinate(true);
                annotationProcessorsCoordinates.add(mnGraal);
                testAnnotationProcessorsCoordinates.add(mnGraal);
            }
        }

        annotationProcessorsCoordinates.sort(Coordinate.COMPARATOR);
        testAnnotationProcessorsCoordinates.sort(Coordinate.COMPARATOR);

        List<MavenPlugin> plugins = generatorContext.getBuildPlugins()
                .stream()
                .filter(MavenPlugin.class::isInstance)
                .map(MavenPlugin.class::cast)
                .sorted(OrderUtil.COMPARATOR)
                .toList();

        return new MavenBuild(generatorContext.getProject().getName(),
                annotationProcessorsCoordinates,
                testAnnotationProcessorsCoordinates,
                dependencies,
                buildProperties.getProperties(),
                plugins,
                MavenRepository.listOf(repositories),
                combineAttribute,
                testCombineAttribute,
                generatorContext.getProfiles(),
                generatorContext.getDependencies().stream().filter(dep -> dep.getScope() == Scope.AOT_PLUGIN).map(DependencyCoordinate::new).toList(),
                testResourcesDependencies(generatorContext));
    }

    @NonNull
    private static List<MavenCoordinate> testResourcesDependencies(@NonNull GeneratorContext generatorContext) {
        return generatorContext.getFeatures().getFeatures()
                .stream()
                .filter(TestResourcesAdditionalModulesProvider.class::isInstance)
                .map(f -> ((TestResourcesAdditionalModulesProvider) f).getTestResourcesDependencies(generatorContext))
                .flatMap(List::stream)
                .distinct()
                .toList();
    }
}

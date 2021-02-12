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

import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.options.BuildTool;

import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class GradleBuildToolDependencyResolver extends DependencyResolver {
    public GradleBuildToolDependencyResolver(MavenCoordinateResolver mavenCoordinateResolver,
                                             AdapterBuilder adapterBuilder) {
        super(mavenCoordinateResolver, adapterBuilder, BuildTool.GRADLE, new GradleDependencyComparator());
    }

    public GradleBuild gradleBuild(GeneratorContext generatorContext) {
        BuildTool buildTool = generatorContext.getBuildTool();

        List<Dependency> dependencies = resolve(generatorContext.getDependencies())
                .stream()
                .filter(dependency -> !dependency.getScope().isPresent() || (dependency.getScope().isPresent() && !dependency.getScope().get().equals(GradleConfiguration.ANNOTATION_PROCESSOR.toString())))
                .collect(Collectors.toList());
        List<MavenCoordinate> annotationProcessors = annotationProcessors(generatorContext.getDependencies());
        return new GradleBuild(buildTool == BuildTool.GRADLE_KOTLIN ? GradleDsl.KOTLIN : GradleDsl.GROOVY, dependencies, annotationProcessors);
    }
}

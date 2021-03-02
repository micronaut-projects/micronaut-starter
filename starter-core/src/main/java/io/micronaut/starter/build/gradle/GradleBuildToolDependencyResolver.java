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
package io.micronaut.starter.build.gradle;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.starter.application.generator.GeneratorContext;
import io.micronaut.starter.build.dependencies.AdapterBuilder;
import io.micronaut.starter.build.dependencies.Dependency;
import io.micronaut.starter.build.dependencies.DependencyResolver;
import io.micronaut.starter.build.dependencies.Phase;
import io.micronaut.starter.build.dependencies.ScopedArtifact;
import io.micronaut.starter.build.maven.MavenCoordinate;
import io.micronaut.starter.build.maven.MavenCoordinateResolver;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.feature.function.oraclefunction.OracleRawFunction;
import io.micronaut.starter.options.BuildTool;

import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
        return new GradleBuild(buildTool == BuildTool.GRADLE_KOTLIN ? GradleDsl.KOTLIN : GradleDsl.GROOVY,
                dependencies,
                annotationProcessors,
                resolveGradlePlugins(generatorContext));
    }

    private List<GradlePlugin> resolveGradlePlugins(@NonNull GeneratorContext generatorContext) {
        List<GradlePlugin> plugins = generatorContext.getBuildPlugins()
                .stream()
                .filter(plugin -> plugin instanceof GradlePlugin)
                .map(plugin -> (GradlePlugin) plugin)
                .collect(Collectors.toList());

        if (generatorContext.getFeatures().language().isKotlin() || generatorContext.getFeatures().testFramework().isKotlinTestFramework()) {
            plugins.add(new GradlePluginCoordinate("org.jetbrains.kotlin.jvm", "kotlin-gradle-plugin"));
            plugins.add(new GradlePluginCoordinate("org.jetbrains.kotlin.kapt", "kotlin-gradle-plugin"));
            plugins.add(new GradlePluginCoordinate("org.jetbrains.kotlin.plugin.allopen", "kotlin-allopen"));
        }

        if (generatorContext.getFeatures().language().isGroovy() || generatorContext.getFeatures().testFramework().isSpock()) {
            plugins.add(new CoreGradlePlugin("groovy"));
        }
        if (generatorContext.getFeatures().mainClass().isPresent() ||
                generatorContext.getFeatures().contains(OracleRawFunction.FEATURE_NAME_ORACLE_RAW_FUNCTION) ||
                generatorContext.getApplicationType() == ApplicationType.DEFAULT && generatorContext.getFeatures().contains(AwsLambda.FEATURE_NAME_AWS_LAMBDA)) {
            plugins.add(new GradlePluginCoordinate("io.micronaut.application", "micronaut-gradle-plugin"));
        } else {
            plugins.add(new GradlePluginCoordinate("io.micronaut.library", "micronaut-gradle-plugin"));
        }
        return plugins.stream()
                .map(plugin -> {
                    if (plugin instanceof GradlePluginCoordinate) {
                        GradlePluginCoordinate gradlePluginCoordinate = (GradlePluginCoordinate) plugin;
                        Optional<MavenCoordinate> coordinateOptional = mavenCoordinateResolver.coordinateByArtifactId(gradlePluginCoordinate.getArtifactId());
                        if (coordinateOptional.isPresent()) {
                            String version = coordinateOptional.get().getVersion();
                            if (version != null) {
                                return new CommunityGradlePlugin(gradlePluginCoordinate.getId(), version);
                            }
                        }
                    }
                    return plugin;
                })
                .filter(plugin -> plugin instanceof CommunityGradlePlugin || plugin instanceof CoreGradlePlugin)
                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    public List<Dependency> resolve(@NonNull Set<ScopedArtifact> artifacts) {
        return artifacts
                .stream()
                .map(scopedArtifact -> mavenCoordinateResolver.resolve(scopedArtifact, true))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(dep -> adapterBuilder.build(dep, buildTool))
                .filter(dep -> dep.getScope().isPresent())
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    public List<MavenCoordinate> annotationProcessors(@NonNull Set<ScopedArtifact> artifacts) {
        return artifacts
                .stream()
                .filter(dep -> dep.getScope().getPhases().contains(Phase.ANNOTATION_PROCESSING))
                .map(scopedArtifact -> mavenCoordinateResolver.resolve(scopedArtifact, true))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .sorted(coordinateComparator)
                .collect(Collectors.toList());
    }
}

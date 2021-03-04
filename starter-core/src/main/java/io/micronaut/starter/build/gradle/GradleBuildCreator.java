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
import io.micronaut.starter.build.Coordinate;
import io.micronaut.starter.build.dependencies.ScopedDependency;
import io.micronaut.starter.build.maven.MavenCoordinateResolver;
import io.micronaut.starter.feature.function.awslambda.AwsLambda;
import io.micronaut.starter.feature.function.oraclefunction.OracleRawFunction;
import javax.inject.Singleton;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class GradleBuildCreator {
    private static final Function<ScopedDependency, Optional<GradleDependency>> TO_GRADLE_DEPENDENCY = dep -> {
        Optional<GradleConfiguration> gradleConfigurationOptional = GradleConfiguration.of(dep.getScope());
        return gradleConfigurationOptional.map(gradleConfiguration ->
                new GradleDependency(gradleConfiguration, dep.getGroupId(), dep.getArtifactId(), dep.getVersion()));
    };

    private final MavenCoordinateResolver mavenCoordinateResolver;
    private final Comparator<GradleDependency> comparator = new GradleDependencyComparator();

    public GradleBuildCreator(MavenCoordinateResolver mavenCoordinateResolver) {
        this.mavenCoordinateResolver = mavenCoordinateResolver;
    }

    @NonNull
    public GradleBuild create(@NonNull GeneratorContext generatorContext) {
        Optional<GradleDsl> gradleDsl = generatorContext.getBuildTool().getGradleDsl();
        if (!gradleDsl.isPresent()) {
            throw new IllegalArgumentException("GradleBuildCreator::create can only be called with Gradle builds");
        }
        return new GradleBuild(gradleDsl.get(),
                resolveDependencies(generatorContext),
                resolveGradlePlugins(generatorContext));
    }

    @NonNull
    private List<GradleDependency> resolveDependencies(@NonNull GeneratorContext generatorContext) {
        List<GradleDependency> dependencies = gradleDependencies(generatorContext.getDependencies().stream());
        dependencies.addAll(gradleDependencies(generatorContext.getDependencyLookups()
                .stream()
                .map(lookup -> mavenCoordinateResolver.resolve(lookup, false))
                .filter(Optional::isPresent)
                .map(Optional::get)));

        dependencies.sort(comparator);
        return dependencies;
    }

    private List<GradleDependency> gradleDependencies(Stream<ScopedDependency> dependencies) {
        return dependencies
                .map(TO_GRADLE_DEPENDENCY)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @NonNull
    private List<GradlePlugin> resolveGradlePlugins(@NonNull GeneratorContext generatorContext) {
        List<GradlePlugin> plugins = generatorContext.getBuildPlugins()
                .stream()
                .filter(plugin -> plugin instanceof GradlePlugin)
                .map(plugin -> (GradlePlugin) plugin)
                .collect(Collectors.toList());
        if (generatorContext.getFeatures().language().isGroovy() || generatorContext.getFeatures().testFramework().isSpock()) {
            plugins.add(new CoreGradlePlugin("groovy"));
        }
        plugins.addAll(resolvePluginsFromLookups(generatorContext));
        return plugins;
    }

    @NonNull
    private List<GradlePluginLookup> collectPluginsFromLookups(@NonNull GeneratorContext generatorContext) {
        List<GradlePluginLookup> pluginLookups = generatorContext.getBuildPluginLookups()
                .stream()
                .filter(lookup -> lookup instanceof GradlePluginLookup)
                .map(lookup -> (GradlePluginLookup) lookup)
                .collect(Collectors.toList());
        if (generatorContext.getFeatures().language().isKotlin() || generatorContext.getFeatures().testFramework().isKotlinTestFramework()) {
            pluginLookups.add(new GradlePluginLookup("org.jetbrains.kotlin.jvm", "kotlin-gradle-plugin"));
            pluginLookups.add(new GradlePluginLookup("org.jetbrains.kotlin.kapt", "kotlin-gradle-plugin"));
            pluginLookups.add(new GradlePluginLookup("org.jetbrains.kotlin.plugin.allopen", "kotlin-allopen"));
        }
        if (generatorContext.getFeatures().mainClass().isPresent() ||
                generatorContext.getFeatures().contains(OracleRawFunction.FEATURE_NAME_ORACLE_RAW_FUNCTION) ||
                generatorContext.getApplicationType() == ApplicationType.DEFAULT && generatorContext.getFeatures().contains(AwsLambda.FEATURE_NAME_AWS_LAMBDA)) {
            pluginLookups.add(new GradlePluginLookup("io.micronaut.application", "micronaut-gradle-plugin"));
        } else {
            pluginLookups.add(new GradlePluginLookup("io.micronaut.library", "micronaut-gradle-plugin"));
        }
        return pluginLookups;

    }

    @NonNull
    private List<GradlePlugin> resolvePluginsFromLookups(@NonNull GeneratorContext generatorContext) {
        return collectPluginsFromLookups(generatorContext)
                .stream()
                .map(lookup -> {
                    Optional<Coordinate> coordinateOptional = mavenCoordinateResolver.coordinateByArtifactId(lookup.getArtifactId());
                    if (coordinateOptional.isPresent()) {
                        String version = coordinateOptional.get().getVersion();
                        if (version != null) {
                            return new CommunityGradlePlugin(lookup.getId(), version);
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}

package io.micronaut.starter.feature.build.gradle

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.NonNull
import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.Property
import io.micronaut.starter.build.RepositoryResolver
import io.micronaut.starter.build.dependencies.Coordinate
import io.micronaut.starter.build.dependencies.CoordinateResolver
import io.micronaut.starter.build.dependencies.DefaultPomDependencyVersionResolver
import io.micronaut.starter.build.gradle.GradleBuildCreator
import io.micronaut.starter.feature.build.MicronautBuildPlugin
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import jakarta.inject.Singleton

import java.util.stream.Collectors

class GradleReplacementSpec extends BeanContextSpec implements CommandOutputFixture {

    @Override
    Map<String, String> getProperties() {
        ["spec.name": "GradleReplacementSpec"]
    }

    void "it is easy to override Micronaut Gradle Plugin version"() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, BuildTool.GRADLE))

        then:
        output["gradle.properties"].contains("micronautVersion=4.0.0-SNAPSHOT")
    }

    @Requires(property = "spec.name", value = "GradleReplacementSpec")
    @Replaces(Gradle.class)
    @Singleton
    static class GradleReplacement extends Gradle  {
        GradleReplacement(GradleBuildCreator dependencyResolver, MicronautBuildPlugin micronautBuildPlugin, RepositoryResolver repositoryResolver) {
            super(dependencyResolver, micronautBuildPlugin, repositoryResolver);
        }

        @Override
        @NonNull
        protected List<Property> gradleProperties(@NonNull GeneratorContext generatorContext) {
            List<Property> properties = super.gradleProperties(generatorContext).stream()
                    .filter(p -> !p.getKey().equals("micronautVersion"))
                    .collect(Collectors.toList());
            properties.add(new Property() {
                @Override
                String getKey() {
                    return "micronautVersion";
                }
                @Override
                String getValue() {
                    return "4.0.0-SNAPSHOT";
                }
            });
            return properties;
        }
    }

    @Requires(property = "spec.name", value = "GradleReplacementSpec")
    @Replaces(CoordinateResolver.class)
    @Singleton
    static class PomDependencyVersionResolverReplacement extends DefaultPomDependencyVersionResolver {
        @Override
        @NonNull
        Optional<Coordinate> resolve(@NonNull String artifactId) {
            Optional<Coordinate> optionalCoordinate = super.resolve(artifactId)
            if (!optionalCoordinate.isPresent()) {
                return optionalCoordinate
            }
            if (artifactId.equals("micronaut-gradle-plugin")) {
                return micronautGradlePluginCoordinate(optionalCoordinate.get())
            } else if (artifactId.equals("shadow")) {
                return shadowCoordinate(optionalCoordinate.get())
            }
            return optionalCoordinate
        }

        @NonNull
        private static Optional<Coordinate> micronautGradlePluginCoordinate(@NonNull Coordinate coordinate) {
            return Optional.of(new Coordinate() {
                @Override
                @NonNull
                String getGroupId() {
                    return coordinate.getGroupId()
                }

                @Override
                @NonNull
                String getArtifactId() {
                    return coordinate.getArtifactId()
                }

                @Override
                String getVersion() {
                    return "4.0.0-SNAPSHOT"
                }

                @Override
                boolean isPom() {
                    return false
                }
            })
        }

        @NonNull
        private static Optional<Coordinate> shadowCoordinate(@NonNull Coordinate coordinate) {
            return Optional.of(new Coordinate() {
                @Override
                @NonNull
                String getGroupId() {
                    return coordinate.getGroupId()
                }

                @Override
                @NonNull
                String getArtifactId() {
                    return coordinate.getArtifactId()
                }

                @Override
                String getVersion() {
                    return "8.1.0"
                }

                @Override
                boolean isPom() {
                    return false
                }
            });
        }
    }
}

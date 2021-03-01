package io.micronaut.starter

import edu.umd.cs.findbugs.annotations.NonNull
import io.micronaut.context.ApplicationContext
import io.micronaut.core.version.SemanticVersion
import io.micronaut.inject.qualifiers.Qualifiers
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.Project
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.dependencies.BuildToolDependencyResolver
import io.micronaut.starter.build.dependencies.Dependency
import io.micronaut.starter.build.dependencies.DependencyContext
import io.micronaut.starter.build.dependencies.GradleBuild
import io.micronaut.starter.build.dependencies.GradleBuildToolDependencyResolver
import io.micronaut.starter.build.dependencies.MavenBuild
import io.micronaut.starter.build.dependencies.MavenBuildToolDependencyResolver
import io.micronaut.starter.build.dependencies.MavenCoordinate
import io.micronaut.starter.build.dependencies.ScopedArtifact
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.feature.Features
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

abstract class ApplicationContextSpec extends Specification implements ProjectFixture, ContextFixture {

    Map<String, Object> getConfiguration() {
        [:]
    }

    @Shared
    @AutoCleanup
    ApplicationContext beanContext = ApplicationContext.run(configuration)

    @Shared
    GradleBuildToolDependencyResolver gradleDependencyResolver = beanContext.getBean(GradleBuildToolDependencyResolver)

    @Shared
    MavenBuildToolDependencyResolver mavenDependencyResolver = beanContext.getBean(MavenBuildToolDependencyResolver)

    MavenBuild mavenBuild(Options options,
                          Features features,
                          Project project,
                          ApplicationType type) {
        GeneratorContext ctx = createGeneratorContextAndApplyFeatures(options, features, project, type)
        mavenDependencyResolver.mavenBuild(ctx)
    }

    GradleBuild gradleBuild(Options options,
                            Features features,
                            Project project,
                            ApplicationType type) {
        GeneratorContext ctx = createGeneratorContextAndApplyFeatures(options, features, project, type)
        gradleDependencyResolver.gradleBuild(ctx)
    }

    GeneratorContext createGeneratorContextAndApplyFeatures(Options options,
                                           Features features,
                                           Project project,
                                           ApplicationType type) {
        GeneratorContext ctx = new GeneratorContext(project, type, options, null, features.features)
        features.features.each {feat -> feat.apply(ctx)}
        ctx
    }

    protected static Optional<SemanticVersion> parsePropertySemanticVersion(String template, String propertyName) {
        List<String> lines = template.split("\n")
        for (String line : lines) {
            if (line.contains("<" + propertyName + ">") && line.contains("</" + propertyName + ">")) {
                String version = line.substring(line.indexOf("<" + propertyName + ">") + ("<" + propertyName + ">").length(), line.indexOf("</" + propertyName + ">"))
                return Optional.of(new SemanticVersion(version))
            }
        }
        return Optional.empty()
    }

    protected static Optional<SemanticVersion> parseDependencySemanticVersion(String template, String groupArtifactId) {
        List<String> lines = template.split("\n")
        for (String line : lines) {
            if (line.contains(groupArtifactId)) {
                String str = line.substring(line.indexOf(groupArtifactId) + groupArtifactId.length() + ":".length())
                String version = str.substring(0, str.indexOf("\")"))
                return Optional.of(new SemanticVersion(version))
            }
        }
        return Optional.empty()
    }
}

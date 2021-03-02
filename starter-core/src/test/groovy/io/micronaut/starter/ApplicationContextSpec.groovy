package io.micronaut.starter

import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import io.micronaut.context.ApplicationContext
import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.Project
import io.micronaut.starter.application.generator.GeneratorContext
import io.micronaut.starter.build.gradle.GradleBuild
import io.micronaut.starter.build.gradle.GradleBuildToolDependencyResolver
import io.micronaut.starter.build.maven.MavenBuild
import io.micronaut.starter.build.maven.MavenBuildToolDependencyResolver
import io.micronaut.starter.feature.Features
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
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

    String gradleTemplate(Language language,
                          List<String> featureNames,
                          ApplicationType type = ApplicationType.DEFAULT,
                          Project project = buildProject(),
                          BuildTool buildTool = BuildTool.GRADLE,
                          TestFramework testFramework = null) {
        TestFramework test = testFramework ?: language.getDefaults().getTest()
        Features features = getFeatures(featureNames, language, test, buildTool, type)
        Options options = new Options(language, test, buildTool)
        GradleBuild build = gradleBuild(options, features, project, type)
        buildGradle.template(type, project, features, build).render().toString()
    }

    String mavenTemplate(Language language, List<String> featureNames, ApplicationType type = ApplicationType.DEFAULT, Project project = buildProject(), BuildTool buildTool = BuildTool.MAVEN) {
        Features features = getFeatures(featureNames, language)
        Options options = new Options(language, buildTool)
        MavenBuild build = mavenBuild(options, features, project, type)
        pom.template(type, project, features, build).render().toString()
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

    protected static Optional<String> parseCommunityGradlePluginVersion(String gradlePluginId, String template) {
        String applyPlugin = 'id("' + gradlePluginId + '") version "'
        List<String> lines = template.split('\n')
        String pluginLine = lines.find { line ->
            line.contains(applyPlugin)
        }
        if (!pluginLine) {
            return Optional.empty()
        }
        String version = pluginLine.substring(pluginLine.indexOf(applyPlugin) + applyPlugin.length())
        if (version.endsWith('"')) {
            version = version.substring(0, version.length() - 1)
        }
        Optional.of(version)
    }
}

package io.micronaut.starter.build.gradle

import io.micronaut.context.exceptions.ConfigurationException
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.build.maven.MavenScope
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework

import java.util.stream.Stream

class GradleBuildTestVerifier implements BuildTestVerifier {

    final String template
    final Language language
    final TestFramework testFramework

    GradleBuildTestVerifier(String template, Language language, TestFramework testFramework) {
        this.template = template
        this.language = language
        this.testFramework = testFramework
    }

    @Override
    boolean hasAnnotationProcessor(String groupId, String artifactId) {
        hasDependency(groupId, artifactId, Scope.ANNOTATION_PROCESSOR)
    }

    @Override
    boolean hasDependency(String groupId, String artifactId, Scope scope) {
        Optional<String> gradleConfigurationNameOptional = GradleConfiguration.of(scope, language, testFramework).map { it.getConfigurationName() }
        if (!gradleConfigurationNameOptional.isPresent()){
            throw new ConfigurationException("cannot match " + scope + " to gradle configuration");
        }
        String gradleConfigurationName = gradleConfigurationNameOptional.get()
        hasDependency(groupId, artifactId, gradleConfigurationName)
    }

    @Override
    boolean hasDependency(String groupId, String artifactId, String scope) {
        String expected = """${scope}("${groupId}:${artifactId}")"""
        template.contains(expected)
    }

    @Override
    boolean hasDependency(String groupId, String artifactId) {
        GradleConfiguration.values().collect { it.getConfigurationName() }.any( {scope ->
            hasDependency(groupId, artifactId, scope)
        })
    }

    @Override
    boolean hasTestResourceDependency(String groupId, String artifactId) {
        hasDependency(groupId, artifactId, "testResourcesService")
    }

    @Override
    boolean hasTestResourceDependency(String artifactId) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    boolean hasDependency(String artifactId) {
        template.contains(artifactId)
    }

    @Override
    boolean hasTestResourceDependencyWithGroupId(String expectedGroupId) {
        throw new UnsupportedOperationException("not yet implemented");
    }
}

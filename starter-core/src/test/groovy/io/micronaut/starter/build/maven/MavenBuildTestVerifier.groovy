package io.micronaut.starter.build.maven

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.XmlParser
import io.micronaut.context.exceptions.ConfigurationException
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope

@CompileStatic
class MavenBuildTestVerifier implements BuildTestVerifier {
    final Node project

    MavenBuildTestVerifier(String template) {
        this.project = new XmlParser().parseText(template)
    }

    @CompileDynamic
    @Override
    boolean hasAnnotationProcessor(String groupId, String artifactId) {
        String expectedCoordinate = "${groupId}:${artifactId}"
        project.build.plugins.plugin.find { it.artifactId.text() == "maven-compiler-plugin" }?.with {
            List<String> coordinates = configuration.annotationProcessorPaths.path.collect { "${it.groupId.text()}:${it.artifactId.text()}".toString() }
            coordinates.contains(expectedCoordinate)
        }
    }

    @Override
    boolean hasBom(String groupId, String artifactId, Scope scope) {
        hasBom(groupId, artifactId, "")
    }

    @CompileDynamic
    @Override
    boolean hasBom(String expectedGroupId, String expectedArtifactId, String expectedScope) {
        project.dependencyManagement.dependencies.dependency.findAll { it.artifactId.text() == expectedArtifactId }.any {
            it.groupId.text() == expectedGroupId && it.type.text() == 'pom' && it.scope.text() == 'import'
        }
    }

    @Override
    boolean hasDependency(String groupId, String artifactId, Scope scope) {
        Optional<String> mavenScopeString = MavenScope.of(scope).map(MavenScope::toString)
        if (!mavenScopeString.isPresent()){
            throw new ConfigurationException("cannot match " + scope + " to maven scope");
        }
        hasDependency(groupId, artifactId, mavenScopeString.get())
    }

    @CompileDynamic
    @Override
    boolean hasDependency(String expectedArtifactId) {
        project.dependencies.dependency.find { it.artifactId.text() == expectedArtifactId }
    }

    @CompileDynamic
    @Override
    boolean hasTestResourceDependencyWithGroupId(String expectedGroupId) {
        def micronautPlugin = project.build.plugins.plugin.find { it.artifactId.text() == 'micronaut-maven-plugin' }
        micronautPlugin.configuration.testResourcesDependencies.dependency.find { it.groupId.text() == expectedGroupId }
    }

    @CompileDynamic
    @Override
    boolean hasDependency(String expectedGroupId, String expectedArtifactId, String expectedScope) {
        project.dependencies.dependency.findAll { it.artifactId.text() == expectedArtifactId }.any {
            it.scope.text() == expectedScope && it.groupId.text() == expectedGroupId
        }
    }

    @CompileDynamic
    @Override
    boolean hasDependency(String expectedGroupId, String expectedArtifactId) {
        project.dependencies.dependency.findAll { it.artifactId.text() == expectedArtifactId }.any {
            it.groupId.text() == expectedGroupId
        }
    }

    @CompileDynamic
    @Override
    boolean hasTestResourceDependency(String expectedGroupId, String expectedArtifactId) {
        def micronautPlugin = project.build.plugins.plugin.find { it.artifactId.text() == 'micronaut-maven-plugin' }
        micronautPlugin.configuration.testResourcesDependencies.dependency.find { it.groupId.text() == expectedGroupId }?.with {
            it.artifactId.text() == expectedArtifactId
        }
    }

    @Override
    boolean hasTestResourceDependency(String artifactId) {
        hasTestResourceDependency("io.micronaut.testresources", artifactId)
    }
}

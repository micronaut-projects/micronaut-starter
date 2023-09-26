package io.micronaut.starter.build.maven

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.xml.XmlParser
import io.micronaut.context.exceptions.ConfigurationException
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.options.Language

@CompileStatic
class MavenBuildTestVerifier implements BuildTestVerifier {
    final Node project
    final Language language

    MavenBuildTestVerifier(String template, Language language) {
        this.project = new XmlParser().parseText(template)
        this.language = language
    }

    @CompileDynamic
    @Override
    boolean hasAnnotationProcessor(String groupId, String artifactId) {
        def result
        String expectedCoordinate = "${groupId}:${artifactId}"
        switch (language) {
            case Language.JAVA:
                project.build.plugins.plugin.find { it.artifactId.text() == "maven-compiler-plugin" }?.with {
                    List<String> coordinates = configuration.annotationProcessorPaths.path
                            .collect { "${it.groupId.text()}:${it.artifactId.text()}".toString() }
                    result = coordinates.contains(expectedCoordinate)
                }
                break
            case Language.GROOVY:
                result = hasDependency(groupId, artifactId)
                break
            case Language.KOTLIN:
                project.build.plugins.plugin.find { it.artifactId.text() == "kotlin-maven-plugin" }?.with {
                    List<String> coordinates = executions.execution.configuration.annotationProcessorPaths.annotationProcessorPath
                            .collect { "${it.groupId.text()}:${it.artifactId.text()}".toString() }
                    result = coordinates.contains(expectedCoordinate)
                }
                break
        }
        result
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
    boolean hasExclusion(String groupId, String artifactId) {
        project.dependencies.dependency.exclusions.exclusion.findAll { it.artifactId.text() == artifactId }.any {
            it.groupId.text() == groupId
        }
    }

    @CompileDynamic
    @Override
    boolean hasTestResourceDependencyWithGroupId(String expectedGroupId) {
        def micronautPlugin = project.build.plugins.plugin.find { it.artifactId.text() == 'micronaut-maven-plugin' }
        micronautPlugin.configuration.testResourcesDependencies.dependency.find { it.groupId.text() == expectedGroupId }
    }

    @Override
    boolean hasBuildPlugin(String id) {
        throw new UnsupportedOperationException("Not implemented")
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

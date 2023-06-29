/*
 * Copyright 2017-2023 original authors
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
    boolean hasAnnotationProcessor(Scope scope, String groupId, String artifactId) {
        String expectedCoordinate = "${groupId}:${artifactId}"
        if (language == Language.KOTLIN) {
            if (scope == Scope.ANNOTATION_PROCESSOR) {
                return project.build.plugins.plugin.find { it.artifactId.text() == "kotlin-maven-plugin" }?.executions.execution.find { it.id.text() == 'kapt' }?.with {
                    List<String> coordinates = configuration.annotationProcessorPaths.annotationProcessorPath.collect { "${it.groupId.text()}:${it.artifactId.text()}".toString() }
                    return coordinates.contains(expectedCoordinate)
                }
            } else if (scope == Scope.TEST_ANNOTATION_PROCESSOR) {
                    return project.build.plugins.plugin.find { it.artifactId.text() == "kotlin-maven-plugin" }?.executions.execution.find { it.id.text() == 'test-kapt' }?.with {
                        List<String> coordinates = configuration.annotationProcessorPaths.annotationProcessorPath.collect { "${it.groupId.text()}:${it.artifactId.text()}".toString() }
                        return coordinates.contains(expectedCoordinate)
                    }
            }
        } else if (language == Language.JAVA){
            return project.build.plugins.plugin.find { it.artifactId.text() == "maven-compiler-plugin" }?.with {
                List<String> coordinates = configuration.annotationProcessorPaths.path.collect { "${it.groupId.text()}:${it.artifactId.text()}".toString() }
                return coordinates.contains(expectedCoordinate)
            }
        }
        return false
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
        if ((scope == Scope.ANNOTATION_PROCESSOR || scope == Scope.TEST_ANNOTATION_PROCESSOR) && language != Language.GROOVY) {
            return hasAnnotationProcessor(scope, groupId, artifactId)
        }
        Optional<String> mavenScopeString = MavenScope.of(scope, language).map(MavenScope::toString)
        if (!mavenScopeString.isPresent()) {
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

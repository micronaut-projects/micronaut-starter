package io.micronaut.starter.build.pyronaut

import groovy.transform.CompileStatic
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope

import java.util.regex.Pattern

@CompileStatic
class PyronautBuildTestVerifier implements BuildTestVerifier {

    final String template
    private final Set<String> runtimeDependencies
    private final Set<String> buildDependencies
    private final Set<String> testDependencies

    PyronautBuildTestVerifier(String template) {
        this.template = template
        this.runtimeDependencies = readDependencies("runtime")
        this.buildDependencies = readDependencies("build")
        this.testDependencies = readDependencies("test")
    }

    @Override
    boolean hasAnnotationProcessor(String groupId, String artifactId) {
        hasDependency(groupId, artifactId, Scope.ANNOTATION_PROCESSOR)
    }

    @Override
    boolean hasTestAnnotationProcessor(String groupId, String artifactId) {
        hasDependency(groupId, artifactId, Scope.TEST_ANNOTATION_PROCESSOR)
    }

    @Override
    boolean hasBom(String groupId, String artifactId, Scope scope) {
        false
    }

    @Override
    boolean hasBom(String groupId, String artifactId, String scope) {
        false
    }

    @Override
    boolean hasDependency(String groupId, String artifactId, Scope scope) {
        dependencies(scope).any { matches(it, groupId, artifactId) }
    }

    @Override
    boolean hasDependency(String groupId, String artifactId, String scope) {
        dependencies(scope).any { matches(it, groupId, artifactId) }
    }

    @Override
    boolean hasDependency(String groupId, String artifactId, Scope scope, String version, boolean isProperty) {
        if (isProperty) {
            return hasDependency(groupId, artifactId, scope)
        }
        dependencies(scope).contains("${groupId}:${artifactId}:${version}".toString())
    }

    @Override
    boolean hasDependency(String groupId, String artifactId, String scope, String version, boolean isProperty) {
        if (isProperty) {
            return hasDependency(groupId, artifactId, scope)
        }
        dependencies(scope).contains("${groupId}:${artifactId}:${version}".toString())
    }

    @Override
    boolean hasDependency(String groupId, String artifactId) {
        [runtimeDependencies, buildDependencies, testDependencies].any { dependencies ->
            dependencies.any { matches(it, groupId, artifactId) }
        }
    }

    @Override
    boolean hasExclusion(String groupId, String artifactId, String excludedGroupId, String excludedArtifactId) {
        false
    }

    @Override
    boolean hasExclusion(String groupId, String artifactId, String excludedGroupId, String excludedArtifactId, Scope scope) {
        false
    }

    @Override
    boolean hasTestResourceDependency(String groupId, String artifactId) {
        hasDependency(groupId, artifactId) || template.contains(artifactId)
    }

    @Override
    boolean hasTestResourceDependency(String artifactId) {
        template.contains(artifactId)
    }

    @Override
    boolean hasDependency(String artifactId) {
        template.contains(artifactId)
    }

    @Override
    boolean hasTestResourceDependencyWithGroupId(String expectedGroupId) {
        template.contains(expectedGroupId)
    }

    @Override
    boolean hasBuildPlugin(String id) {
        id == "io.micronaut.test-resources" && template.contains("[tool.pyronaut.test-resources]")
    }

    private Set<String> dependencies(Scope scope) {
        if (scope == Scope.ANNOTATION_PROCESSOR || scope == Scope.COMPILE_ONLY) {
            return buildDependencies
        }
        if (scope == Scope.TEST || scope == Scope.TEST_RUNTIME || scope == Scope.TEST_COMPILE_ONLY
                || scope == Scope.TEST_ANNOTATION_PROCESSOR || scope == Scope.TEST_RESOURCES_SERVICE) {
            return testDependencies
        }
        return runtimeDependencies
    }

    private Set<String> readDependencies(String scope) {
        def matcher = Pattern.compile("(?ms)^\\s*${Pattern.quote(scope)}\\s*=\\s*\\[(.*?)^\\s*\\]")
                .matcher(template)
        if (!matcher.find()) {
            return Collections.emptySet()
        }
        def quoted = Pattern.compile('"([^"]+)"').matcher(matcher.group(1))
        Set<String> values = new LinkedHashSet<>()
        while (quoted.find()) {
            values.add(quoted.group(1))
        }
        values
    }

    private Set<String> dependencies(String scope) {
        switch (scope) {
            case "runtime":
                return runtimeDependencies
            case "build":
                return buildDependencies
            case "test":
                return testDependencies
            default:
                return Collections.emptySet()
        }
    }

    private static boolean matches(String coordinate, String groupId, String artifactId) {
        coordinate == "${groupId}:${artifactId}".toString() || coordinate.startsWith("${groupId}:${artifactId}:".toString())
    }
}

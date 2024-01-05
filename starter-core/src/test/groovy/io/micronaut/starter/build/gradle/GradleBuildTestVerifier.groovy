package io.micronaut.starter.build.gradle

import io.micronaut.context.exceptions.ConfigurationException
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework

import java.util.regex.Pattern

class GradleBuildTestVerifier implements BuildTestVerifier {

    final BuildTool buildTool
    final String template
    final Language language
    final TestFramework testFramework

    GradleBuildTestVerifier(BuildTool buildTool, String template, Language language, TestFramework testFramework) {
        this.buildTool = buildTool
        this.template = template
        this.language = language
        this.testFramework = testFramework
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
        Optional<String> gradleConfigurationNameOptional = GradleConfiguration.of(scope, language, testFramework, null).map { it.getConfigurationName() }
        if (!gradleConfigurationNameOptional.isPresent()){
            throw new ConfigurationException("cannot match " + scope + " to gradle configuration");
        }
        String gradleConfigurationName = gradleConfigurationNameOptional.get()
        hasBom(groupId, artifactId, gradleConfigurationName)
    }

    @Override
    boolean hasBom(String groupId, String artifactId, String scope) {
        // gradle and gradle kotlin are slightly different `implementation platform(` vs `implementation(platform(`
        Pattern.compile("""${scope}[\\s(]platform\\("${groupId}:${artifactId}:""")
                .matcher(template).find()
    }

    @Override
    boolean hasDependency(String groupId, String artifactId, Scope scope) {
        Optional<String> gradleConfigurationNameOptional = GradleConfiguration.of(scope, language, testFramework, null).map { it.getConfigurationName() }
        if (!gradleConfigurationNameOptional.isPresent()){
            throw new ConfigurationException("cannot match " + scope + " to gradle configuration");
        }
        String gradleConfigurationName = gradleConfigurationNameOptional.get()
        hasDependency(groupId, artifactId, gradleConfigurationName)
    }

    @Override
    boolean hasDependency(String groupId, String artifactId, String scope) {
        String regex = /(?s).*${scope}\("${groupId}:${artifactId}(?::.+)?\"\).*/
        template.matches(Pattern.compile(regex))
    }

    @Override
    boolean hasDependency(String groupId, String artifactId, Scope scope, String version, boolean isProperty) {
        return GradleConfiguration.of(scope, language, testFramework, null)
                .map { hasDependency(groupId, artifactId, it.getConfigurationName(), version, isProperty) }
                .orElseThrow { new ConfigurationException("cannot match " + scope + " to gradle configuration") }
    }

    @Override
    boolean hasDependency(String groupId, String artifactId, String scope, String version, boolean isProperty) {
        if (isProperty) {
            // Gradle will use the version specified in the BOM
            return hasDependency(groupId, artifactId, scope)
        }
        String regex = /(?s).*${scope}\("${groupId}:${artifactId}:${version}\"\).*/
        template.matches(Pattern.compile(regex))
    }

    @Override
    boolean hasDependency(String groupId, String artifactId) {
        GradleConfiguration.values().collect { it.getConfigurationName() }.any( {scope ->
            hasDependency(groupId, artifactId, scope)
        })
    }

    @Override
    boolean hasExclusion(String groupId, String artifactId, String excludedGroupId, String excludedArtifactId) {
        hasExclusion(groupId, artifactId, excludedGroupId, excludedArtifactId, Scope.COMPILE)
    }

    @Override
    boolean hasExclusion(String groupId, String artifactId, String excludedGroupId, String excludedArtifactId, Scope scope) {
        Optional<String> gradleConfigurationNameOptional = GradleConfiguration.of(scope, language, testFramework, null).map { it.getConfigurationName() }
        if (!gradleConfigurationNameOptional.isPresent()){
            throw new ConfigurationException("cannot match " + scope + " to gradle configuration");
        }
        String gradleConfigurationName = gradleConfigurationNameOptional.get()
        String pattern = /(?s).*${gradleConfigurationName}\("${groupId.replace(".", "\\.")}:${artifactId}"\)\s*\{(.+?)\}/
        println pattern
        def match = template =~ pattern
        if (match.size() > 0) {
            if (buildTool == BuildTool.GRADLE) {
                return match[0][1].contains("exclude(group: \"${excludedGroupId}\", module: \"${excludedArtifactId}\")")
            } else {
                return match[0][1].contains("exclude(group = \"${excludedGroupId}\", module = \"${excludedArtifactId}\"")
            }
        }
        return false
    }

    @Override
    boolean hasTestResourceDependency(String groupId, String artifactId) {
        hasDependency(groupId, artifactId, Scope.TEST_RESOURCES_SERVICE)
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

    @Override
    boolean hasBuildPlugin(String id) {
        return template.contains("id(\"" + id + "\")")
    }
}

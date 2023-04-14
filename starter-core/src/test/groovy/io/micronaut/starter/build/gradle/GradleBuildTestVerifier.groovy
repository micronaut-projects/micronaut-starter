package io.micronaut.starter.build.gradle

import io.micronaut.context.exceptions.ConfigurationException
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import java.util.regex.Pattern

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
    boolean hasBom(String groupId, String artifactId, Scope scope) {
        Optional<String> gradleConfigurationNameOptional = GradleConfiguration.of(scope, language, testFramework).map { it.getConfigurationName() }
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
        Optional<String> gradleConfigurationNameOptional = GradleConfiguration.of(scope, language, testFramework).map { it.getConfigurationName() }
        if (!gradleConfigurationNameOptional.isPresent()){
            throw new ConfigurationException("cannot match " + scope + " to gradle configuration");
        }
        String gradleConfigurationName = gradleConfigurationNameOptional.get()
        hasDependency(groupId, artifactId, gradleConfigurationName)
    }

    @Override
    boolean hasDependency(String groupId, String artifactId, String scope) {
        // this needs to support ignoring versions for non-managed dependencies
        // but it also needs to return false for ("com.foo":"foo") when ("com.foo":"foo-bar") is present
        String expected = /(?s).*${scope}\("${groupId}:${artifactId}(:.+)?\"\).*/
        template.matches(Pattern.compile(expected))
    }

    @Override
    boolean hasDependency(String groupId, String artifactId) {
        GradleConfiguration.values().collect { it.getConfigurationName() }.any( {scope ->
            hasDependency(groupId, artifactId, scope)
        })
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

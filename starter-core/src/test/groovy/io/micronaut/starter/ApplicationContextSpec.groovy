package io.micronaut.starter

import io.micronaut.context.ApplicationContext
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.version.SemanticVersion
import io.micronaut.starter.feature.Feature
import io.micronaut.starter.fixture.ContextFixture
import io.micronaut.starter.fixture.ProjectFixture
import io.micronaut.starter.options.Language
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

    @NonNull
    Optional<Feature> findFeatureByName(@NonNull String featureName) {
        beanContext.streamOfType(Feature)
                .filter(it -> it.getName() == featureName)
                .findFirst()
    }

    protected void assertAnnotationProcessorInGradleTemplate(String template, String mavenCoordinate, Language language) {
        if (language == Language.JAVA) {
            assert template.contains('annotationProcessor("' + mavenCoordinate +'")')
        } else if (language == Language.GROOVY) {
            assert template.contains('compileOnly("' + mavenCoordinate + '")')
        } else if (language == Language.KOTLIN) {
            assert template.contains('kapt("' + mavenCoordinate + '")')
        }
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

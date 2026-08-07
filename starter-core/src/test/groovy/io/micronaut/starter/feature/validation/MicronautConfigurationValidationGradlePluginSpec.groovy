package io.micronaut.starter.feature.validation

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.StarterCoordinates
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import jakarta.inject.Inject
import spock.lang.Subject

import static io.micronaut.starter.application.ApplicationType.DEFAULT

class MicronautConfigurationValidationGradlePluginSpec extends ApplicationContextSpec implements CommandOutputFixture {

    private static final String GRADLE_PLUGIN_VERSION = StarterCoordinates.MICRONAUT_GRADLE_PLUGIN.version
    private static final String CONFIGURATION_VALIDATION_PLUGIN = 'id("io.micronaut.configuration.validation") version "' + GRADLE_PLUGIN_VERSION + '"'
    private static final String APP_PLUGIN = 'id("io.micronaut.application") version "' + GRADLE_PLUGIN_VERSION + '"'

    @Subject
    @Inject
    MicronautConfigurationValidationGradlePlugin feature = beanContext.getBean(MicronautConfigurationValidationGradlePlugin)

    void 'application with #buildTool has configuration validation gradle plugin for language=#language'(BuildTool buildTool, Language language) {
        when:
        String output =   new BuildBuilder(beanContext, buildTool)
                .language(language)
                .applicationType(ApplicationType.MESSAGING)
                .features(["mqtt"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, output)

        then:
        output.contains(CONFIGURATION_VALIDATION_PLUGIN)
        verifier.hasBuildPlugin(MicronautConfigurationValidationGradlePlugin.MICRONAUT_GRADLE_PLUGIN_CONFIGURATION_VALIDATION_ID)
        output.contains("configurationValidation {")

        where:
        [buildTool, language] << [BuildTool.valuesGradle(), Language.values().toList()].combinations()
    }

    void 'application with #buildTool has configuration validation gradle plugin for language=#language'(BuildTool buildTool, Language language) {
        when:
        String output = build(buildTool, language)
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, language, output)

        then:
        output.contains(CONFIGURATION_VALIDATION_PLUGIN)
        verifier.hasBuildPlugin(MicronautConfigurationValidationGradlePlugin.MICRONAUT_GRADLE_PLUGIN_CONFIGURATION_VALIDATION_ID)
        output.contains("configurationValidation {")

        where:
        [buildTool, language] << [BuildTool.valuesGradle(), Language.values().toList()].combinations()
    }

    void 'configuration validation renders suppressions for #buildTool'(BuildTool buildTool, String expectedSuppressionLine) {
        when:
        String output = build(buildTool, Language.JAVA)

        then:
        output.contains('configurationValidation {')
        output.contains(expectedSuppressionLine)

        where:
        buildTool              | expectedSuppressionLine
        BuildTool.GRADLE       | 'suppressions.set(["datasources.default.db-type"])'
        BuildTool.GRADLE_KOTLIN| 'suppressions.set(listOf("datasources.default.db-type"))'
    }

    void 'configuration validation gradle plugin order is correct for language=#language with #buildTool'(BuildTool buildTool, Language language) {
        when:
        String output = build(buildTool, language)

        then:
        output.contains(APP_PLUGIN)
        output.contains(CONFIGURATION_VALIDATION_PLUGIN)
        output.indexOf(APP_PLUGIN) < output.indexOf(CONFIGURATION_VALIDATION_PLUGIN)

        where:
        [buildTool, language] << [BuildTool.valuesGradle(), Language.values().toList()].combinations()
    }

    void 'micronaut configuration validation gradle plugin is a default feature for #buildTool'(BuildTool buildTool) {
        when:
        String output = build(buildTool, Language.JAVA, [])

        then:
        output.contains(CONFIGURATION_VALIDATION_PLUGIN)

        where:
        buildTool << BuildTool.valuesGradle()
    }

    void 'micronaut configuration validation gradle plugin is not visible'() {
        expect:
        !feature.visible
    }

    private String build(BuildTool buildTool, Language language, List<String> features = []) {
        new BuildBuilder(beanContext, buildTool)
                .language(language)
                .applicationType(DEFAULT)
                .features(features)
                .render()
    }
}

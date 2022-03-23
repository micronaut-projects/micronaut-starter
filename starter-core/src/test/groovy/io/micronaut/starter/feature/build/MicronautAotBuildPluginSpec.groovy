package io.micronaut.starter.feature.build

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

import static io.micronaut.starter.application.ApplicationType.DEFAULT
import static io.micronaut.starter.options.BuildTool.GRADLE
import static io.micronaut.starter.options.BuildTool.GRADLE_KOTLIN
import static io.micronaut.starter.options.BuildTool.MAVEN

class MicronautAotBuildPluginSpec extends ApplicationContextSpec implements CommandOutputFixture {

    private static final String GRADLE_PLUGIN_VERSION = '3.3.0'
    private static final String AOT_PLUGIN = 'id("io.micronaut.aot") version "' + GRADLE_PLUGIN_VERSION + '"'
    private static final String APP_PLUGIN = 'id("io.micronaut.application") version "' + GRADLE_PLUGIN_VERSION + '"'

    @Unroll
    void 'application with gradle and feature micronaut-aot for language=#language'() {
        when:
        String output = build(GRADLE, language)

        then:
        output.contains(AOT_PLUGIN)
        output.contains('aot {')
        output.contains('optimizeServiceLoading = true')
        output.contains('convertYamlToJava = true')
        output.contains('precomputeOperations = true')
        output.contains('cacheEnvironment = true')
        output.contains('optimizeClassLoading = true')
        output.contains('deduceEnvironment = true')
        output.contains("version = '1.0.0-M7'")

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'application with gradle and feature micronaut-aot for language=#language and Kotlin DSL'() {
        when:
        String output = build(GRADLE_KOTLIN, language)

        then:
        output.contains(AOT_PLUGIN)
        output.contains('aot {')
        output.contains('optimizeServiceLoading.set(true)')
        output.contains('convertYamlToJava.set(true)')
        output.contains('precomputeOperations.set(true)')
        output.contains('cacheEnvironment.set(true)')
        output.contains('optimizeClassLoading.set(true)')
        output.contains('deduceEnvironment.set(true)')
        output.contains('version.set("1.0.0-M7")')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'order is correct in application with gradle and feature micronaut-aot for language=#language'() {
        when:
        String output = build(GRADLE, language)

        then:
        output.contains(AOT_PLUGIN)
        output.contains(APP_PLUGIN)
        output.indexOf(APP_PLUGIN) < output.indexOf(AOT_PLUGIN)

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'order is correct in application with gradle and feature micronaut-aot for language=#language and Kotlin DSL'() {
        when:
        String output = build(GRADLE_KOTLIN, language)

        then:
        output.contains(AOT_PLUGIN)
        output.contains(APP_PLUGIN)
        output.indexOf(APP_PLUGIN) < output.indexOf(AOT_PLUGIN)

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'application with maven and feature micronaut-aot for language=#language'() {
        when:
        String output = build(MAVEN, language)

        then:
        output.contains("<micronaut.aot.packageName>example.micronaut</micronaut.aot.packageName>")

        where:
        language << Language.values().toList()
    }

    private String build(BuildTool buildTool, Language language) {
        new BuildBuilder(beanContext, buildTool)
                .language(language)
                .applicationType(DEFAULT)
                .features(['micronaut-aot'])
                .render()
    }
}

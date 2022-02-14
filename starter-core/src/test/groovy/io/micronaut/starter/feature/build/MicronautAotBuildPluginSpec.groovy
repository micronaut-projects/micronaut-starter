package io.micronaut.starter.feature.build

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class MicronautAotBuildPluginSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Unroll
    void 'function with gradle and feature micronaut-aot for language=#language'() {
        when:
        String build = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .language(language)
                .applicationType(ApplicationType.DEFAULT)
                .features(['micronaut-aot'])
                .render()

        then:
        build.contains('id("io.micronaut.aot") version "3.2.1"')
        build.contains('aot {')
        build.contains('optimizeServiceLoading = true')
        build.contains('convertYamlToJava = true')
        build.contains('precomputeOperations = true')
        build.contains('cacheEnvironment = true')
        build.contains('optimizeClassLoading = true')
        build.contains('deduceEnvironment = true')
        build.contains('version = \'1.0.0-M7\'')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'function with gradle and feature micronaut-aot for language=#language and Kotlin DSL'() {
        when:
        String build = new BuildBuilder(beanContext, BuildTool.GRADLE_KOTLIN)
                .language(language)
                .applicationType(ApplicationType.DEFAULT)
                .features(['micronaut-aot'])
                .render()

        then:
        build.contains('id("io.micronaut.aot") version "3.2.1"')
        build.contains('aot {')
        build.contains('optimizeServiceLoading.set(true)')
        build.contains('convertYamlToJava.set(true)')
        build.contains('precomputeOperations.set(true)')
        build.contains('cacheEnvironment.set(true)')
        build.contains('optimizeClassLoading.set(true)')
        build.contains('deduceEnvironment.set(true)')
        build.contains('version.set("1.0.0-M7")')

        where:
        language << Language.values().toList()
    }
}

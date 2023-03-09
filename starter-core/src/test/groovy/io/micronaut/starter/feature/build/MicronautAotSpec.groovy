package io.micronaut.starter.feature.build

import io.micronaut.starter.feature.build.maven.templates.aot
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.feature.graalvm.GraalVM
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

import static io.micronaut.starter.application.ApplicationType.DEFAULT
import static io.micronaut.starter.options.BuildTool.GRADLE
import static io.micronaut.starter.options.BuildTool.GRADLE_KOTLIN
import static io.micronaut.starter.options.BuildTool.MAVEN

class MicronautAotSpec extends ApplicationContextSpec implements CommandOutputFixture {

    private static final String GRADLE_PLUGIN_VERSION = '3.7.4'
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
        output.contains('optimizeNetty = true')

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
        output.contains('optimizeNetty.set(true)')

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
        output.contains("<micronaut.aot.packageName>example.micronaut.aot.generated</micronaut.aot.packageName>")
        output.contains("<micronaut.aot.enabled>true</micronaut.aot.enabled>")
        output.contains('<configFile>aot-${packaging}.properties</configFile>')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'aot-#packaging properties file is correct'(String packaging) {
        when:
        String aotProperties = aot.template(packaging).render().toString()
        String expected = getClass().getResource("/expected-aot-${packaging}.properties").text

        then:
        aotProperties == expected

        where:
        packaging << ['jar', 'native-image']
    }

    void 'aot properties file is generated when aot feature is selected'() {
        given:
        def output = generate(DEFAULT, mavenAotOptions(), [MicronautAot.FEATURE_NAME_AOT])
        String expected = getClass().getResource("/expected-aot-jar.properties").text

        expect:
        output.containsKey('aot-jar.properties')
        output["aot-jar.properties"] == expected
        !output.containsKey('aot-native-image.properties')
    }

    void 'aot properties files are generated when aot and graalvm features are selected'() {
        given:
        def output = generate(DEFAULT, mavenAotOptions(), [MicronautAot.FEATURE_NAME_AOT, GraalVM.FEATURE_NAME_GRAALVM])
        String expectedAotJar = getClass().getResource("/expected-aot-jar.properties").text
        String expectedAotNativeImage = getClass().getResource("/expected-aot-native-image.properties").text

        expect:
        output.containsKey('aot-jar.properties')
        output["aot-jar.properties"] == expectedAotJar

        output.containsKey('aot-native-image.properties')
        output["aot-native-image.properties"] == expectedAotNativeImage
    }

    private String build(BuildTool buildTool, Language language) {
        new BuildBuilder(beanContext, buildTool)
                .language(language)
                .applicationType(DEFAULT)
                .features([MicronautAot.FEATURE_NAME_AOT])
                .render()
    }

    private Options mavenAotOptions() {
        return new Options(Language.DEFAULT_OPTION, TestFramework.DEFAULT_OPTION, MAVEN)
    }
}

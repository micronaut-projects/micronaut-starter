package io.micronaut.starter.feature.build

import groovy.xml.XmlSlurper
import io.micronaut.core.util.StringUtils
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.build.dependencies.StarterCoordinates
import io.micronaut.starter.feature.DefaultFeature
import io.micronaut.starter.feature.graalvm.GraalVM
import io.micronaut.starter.feature.security.SecurityJWT
import io.micronaut.starter.feature.security.SecurityOAuth2
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.VersionInfo
import jakarta.inject.Inject
import spock.lang.Subject
import spock.lang.Unroll

import static io.micronaut.starter.application.ApplicationType.DEFAULT
import static io.micronaut.starter.options.BuildTool.GRADLE
import static io.micronaut.starter.options.BuildTool.GRADLE_KOTLIN
import static io.micronaut.starter.options.BuildTool.MAVEN

class MicronautAotSpec extends ApplicationContextSpec implements CommandOutputFixture {

    private static final String GRADLE_PLUGIN_VERSION = StarterCoordinates.MICRONAUT_GRADLE_PLUGIN.version
    private static final String AOT_PLUGIN = 'id("io.micronaut.aot") version "' + GRADLE_PLUGIN_VERSION + '"'
    private static final String APP_PLUGIN = 'id("io.micronaut.application") version "' + GRADLE_PLUGIN_VERSION + '"'

    @Subject
    @Inject
    MicronautAot feature = beanContext.getBean(MicronautAot)

    void '#language application with aot and oauth adds security aot keys'(Language language) {
        when:
        String output = build(GRADLE, language, [MicronautAot.FEATURE_NAME_AOT, SecurityOAuth2.NAME])

        then:
        output.contains(AOT_PLUGIN)
        output.contains('"micronaut.security.jwks.enabled","false"')
        output.contains('"micronaut.security.openid-configuration.enabled","false"')

        where:
        language << Language.values().toList()
    }

    void 'application with aot and oauth adds platform dependency for the aot scope -- language=#language (#tool)'(Language language, BuildTool tool) {
        when:
        String template = new BuildBuilder(beanContext, tool)
                .language(language)
                .features([MicronautAot.FEATURE_NAME_AOT, SecurityOAuth2.NAME])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(tool, language, template)

        then:
        verifier.hasBuildPlugin("io.micronaut.aot")
        verifier.hasBom("io.micronaut.platform", "micronaut-platform", Scope.AOT_PLUGIN)
        verifier.hasDependency("io.micronaut.security", "micronaut-security-aot", Scope.AOT_PLUGIN)
        and:
        template.contains("io.micronaut.platform:micronaut-platform:$VersionInfo.micronautVersion")

        where:
        [language, tool] << [Language.values().toList(), BuildTool.valuesGradle()].combinations()
    }

    void 'application with aot and jwt adds security jwks aot key language=#language'(Language language) {
        when:
        String output = build(GRADLE, language, [MicronautAot.FEATURE_NAME_AOT, SecurityJWT.NAME])

        then:
        output.contains(AOT_PLUGIN)
        output.contains("\"micronaut.security.jwks.enabled\",\"false\"")

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'application with gradle and feature micronaut-aot for language=#language'() {
        when:
        String output = build(GRADLE, language)

        then:
        output.contains(AOT_PLUGIN)
        output.contains('aot {')
        output.contains('optimizeServiceLoading = false')
        output.contains('convertYamlToJava = false')
        output.contains('precomputeOperations = true')
        output.contains('cacheEnvironment = true')
        output.contains('optimizeClassLoading = true')
        output.contains('deduceEnvironment = true')
        output.contains('optimizeNetty = true')
        output.contains('replaceLogbackXml = true')

        where:
        language << Language.values().toList()
    }

    @Unroll
    void 'AOT is enabled by default'() {
        when:
        String output = build(GRADLE_KOTLIN, Language.JAVA, [])

        then:
        output.contains(AOT_PLUGIN)
        output.contains('aot {')
    }

    @Unroll
    void 'application with gradle and feature micronaut-aot for language=#language and Kotlin DSL'() {
        when:
        String output = build(GRADLE_KOTLIN, language)

        then:
        output.contains(AOT_PLUGIN)
        output.contains('aot {')
        output.contains('optimizeServiceLoading.set(false)')
        output.contains('convertYamlToJava.set(false)')
        output.contains('precomputeOperations.set(true)')
        output.contains('cacheEnvironment.set(true)')
        output.contains('optimizeClassLoading.set(true)')
        output.contains('deduceEnvironment.set(true)')
        output.contains('optimizeNetty.set(true)')
        output.contains('replaceLogbackXml.set(true)')

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

        when:
        String expected = "<micronaut.aot.enabled>${(feature instanceof DefaultFeature ? StringUtils.FALSE : StringUtils.TRUE)}</micronaut.aot.enabled>"

        then:
        output.contains(expected)

        and:
        output.contains('<configFile>aot-${packaging}.properties</configFile>')

        where:
        language << Language.values().toList()
    }

    void 'aot properties file is generated when aot feature is selected'() {
        given:
        def output = generate(DEFAULT, mavenAotOptions(), [MicronautAot.FEATURE_NAME_AOT])
        String expected = getClass().getResource("/expected-aot-jar.properties").text

        expect:
        output.containsKey('aot-jar.properties')
        output["aot-jar.properties"].trim() == expected.trim()
        !output.containsKey('aot-native-image.properties')
    }

    void 'aot properties files are generated when aot and graalvm features are selected'() {
        given:
        def output = generate(DEFAULT, mavenAotOptions(), [MicronautAot.FEATURE_NAME_AOT, GraalVM.FEATURE_NAME_GRAALVM])
        String expectedAotJar = getClass().getResource("/expected-aot-jar.properties").text
        String expectedAotNativeImage = getClass().getResource("/expected-aot-native-image.properties").text

        expect:
        output.containsKey('aot-jar.properties')
        output["aot-jar.properties"].trim() == expectedAotJar.trim()

        output.containsKey('aot-native-image.properties')
        output["aot-native-image.properties"].trim() == expectedAotNativeImage.trim()
    }

    void 'aot properties files are generated when aot security-jwt features are selected'() {
        when:
        Map<String,String> output = generate(DEFAULT, mavenAotOptions(), [MicronautAot.FEATURE_NAME_AOT, SecurityJWT.NAME])
        then:
        output.containsKey('aot-jar.properties')
        output["aot-jar.properties"].contains("It fetches remote Json Web Key Set at Build Time. https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html#aotJwks")
        output["aot-jar.properties"].contains("micronaut.security.jwks.enabled=false")
    }

    void 'aot properties files are generated when aot security-oauth2 features are selected'() {
        when:
        Map<String,String> output = generate(DEFAULT, mavenAotOptions(), [MicronautAot.FEATURE_NAME_AOT, SecurityOAuth2.NAME])
        def pom = new XmlSlurper().parseText(output.'pom.xml')

        then:
        output.containsKey('aot-jar.properties')
        output["aot-jar.properties"].contains("It fetches remote Json Web Key Set at Build Time. https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html#aotJwks")
        output["aot-jar.properties"].contains("micronaut.security.jwks.enabled=false")

        output["aot-jar.properties"].contains("It fetches OpenID Connect metadata at Build time. https://micronaut-projects.github.io/micronaut-security/latest/guide/index.html#aotOpenidConfiguration")
        output["aot-jar.properties"].contains("micronaut.security.openid-configuration.enabled=false")

        with(pom.build.plugins.plugin.find { it.artifactId == 'micronaut-maven-plugin' }) {
            it
            with(configuration.aotDependencies.dependency.find { it.artifactId == 'micronaut-security-aot' }) {
                it
                groupId == 'io.micronaut.security'
                version == '${micronaut.security.version}'
            }
        }
    }

    private String build(BuildTool buildTool, Language language, List<String> features = [MicronautAot.FEATURE_NAME_AOT]) {
        new BuildBuilder(beanContext, buildTool)
                .language(language)
                .applicationType(DEFAULT)
                .features(features)
                .render()
    }

    private Options mavenAotOptions() {
        return new Options(Language.DEFAULT_OPTION, TestFramework.DEFAULT_OPTION, MAVEN)
    }
}

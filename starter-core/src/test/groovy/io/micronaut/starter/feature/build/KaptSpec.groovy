package io.micronaut.starter.feature.build

import groovy.xml.XmlParser
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.LanguageSpecificFeature
import io.micronaut.starter.feature.OneOfFeature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject

import java.util.stream.Collectors

class KaptSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    Kapt kapt = beanContext.getBean(Kapt)

    void "kapt isOneOfFeature "() {
        expect:
        kapt instanceof OneOfFeature
    }

    void "ksp does not requires kotlin"() {
        expect:
        !(kapt instanceof LanguageSpecificFeature)
    }

    void 'kapt feature is in the cloud category'() {
        expect:
        kapt.category == Category.LANGUAGES
    }

    void 'kapt feature is not preview'() {
        expect:
        !kapt.isPreview()
    }

    void 'kapt supports every application type'(ApplicationType applicationType) {
        expect:
        kapt.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void 'kapt defines documentation'() {
        expect:
        kapt.micronautDocumentation
        kapt.thirdPartyDocumentation
    }

    void "test #buildTool kapt feature adds build plugin"(BuildTool buildTool) {
        when:
        Language language = Language.KOTLIN
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(["kapt"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasBuildPlugin("org.jetbrains.kotlin.jvm")
        !verifier.hasBuildPlugin("com.google.devtools.ksp")
        verifier.hasBuildPlugin("org.jetbrains.kotlin.plugin.allopen")
        verifier.hasBuildPlugin("org.jetbrains.kotlin.kapt")

        where:
        buildTool << BuildTool.valuesGradle()
    }

    void "for java 21, with #buildTool and Kapt we add the add-opens hack"() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options(Language.KOTLIN, TestFramework.DEFAULT_OPTION, buildTool, JdkVersion.JDK_21))

        then:
        output."gradle.properties".contains(KotlinSupportFeature.JDK_21_KAPT_MODULES.lines().collect(Collectors.joining(" \\${System.lineSeparator()}  ")))

        where:
        buildTool << BuildTool.valuesGradle()
    }

    void "for java 21, maven defaults to kapt in a kotlin build and adds the add-opens hack"() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options(Language.KOTLIN, TestFramework.DEFAULT_OPTION, BuildTool.MAVEN, JdkVersion.JDK_21))
        def pom = new XmlParser().parseText(output."pom.xml")

        then: 'there is a kapt execution in the kotlin plugin'
        pom.build.plugins.plugin.find { it.artifactId.text() == 'kotlin-maven-plugin' }.executions.execution.find { it.id.text() == 'kapt' }

        and: 'the config file is added in the right place'
        output.".mvn/jvm.config" == KotlinSupportFeature.JDK_21_KAPT_MODULES
    }
}

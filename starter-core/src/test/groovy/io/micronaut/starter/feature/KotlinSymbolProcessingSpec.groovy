package io.micronaut.starter.feature

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject

import java.util.stream.Collectors

class KotlinSymbolProcessingSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Shared
    @Subject
    KotlinSymbolProcessing ksp = beanContext.getBean(KotlinSymbolProcessing)

    void "ksp isOneOfFeature "() {
        expect:
        ksp instanceof OneOfFeature
    }

    void "ksp does not requires kotlin"() {
        expect:
        !(ksp instanceof LanguageSpecificFeature)
    }

    void 'ksp feature is in the cloud category'() {
        expect:
        ksp.category == Category.LANGUAGES
    }

    void 'ksp feature is preview'() {
        expect:
        ksp.isPreview()
    }

    void 'ksp supports every application type'(ApplicationType applicationType) {
        expect:
        ksp.supports(applicationType)

        where:
        applicationType << ApplicationType.values()
    }

    void 'ksp defines documentation'() {
        expect:
        ksp.micronautDocumentation
        ksp.thirdPartyDocumentation
    }

    void "test #buildTool ksp feature adds build plugin"(BuildTool buildTool) {
        when:
        Language language = Language.KOTLIN
        String template = new BuildBuilder(beanContext, buildTool)
                .language(language)
                .features(["ksp"])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(buildTool, template)

        then:
        verifier.hasBuildPlugin("org.jetbrains.kotlin.jvm")
        verifier.hasBuildPlugin("com.google.devtools.ksp")
        verifier.hasBuildPlugin("org.jetbrains.kotlin.plugin.allopen")
        !verifier.hasBuildPlugin("org.jetbrains.kotlin.kapt")

        where:
        buildTool << BuildTool.valuesGradle()
    }

    void "org.gradle.jvmargs is only added for KSP"() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, TestFramework.DEFAULT_OPTION, BuildTool.GRADLE_KOTLIN, JdkVersion.JDK_17))

        then:
        output."gradle.properties" =~ /(?m)^micronautVersion=.+/
        !(output."gradle.properties" =~ /(?m)^org.gradle.jvmargs=.+/)

        where:
        buildTool << BuildTool.valuesGradle()
    }

    void "org.gradle.jvmargs is added for KSP"() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, new Options(Language.KOTLIN, TestFramework.DEFAULT_OPTION, BuildTool.GRADLE_KOTLIN, JdkVersion.JDK_17))

        then:
        output."gradle.properties" =~ /(?m)^micronautVersion=.+/
        output."gradle.properties" =~ /(?m)^org.gradle.jvmargs=.+/

        where:
        buildTool << BuildTool.valuesGradle()
    }
}

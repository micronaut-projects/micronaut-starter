package io.micronaut.starter.feature

import io.micronaut.projectgen.core.feature.LanguageSpecificFeature
import io.micronaut.projectgen.core.feature.OneOfFeature
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
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
        ksp.supports(MicronautOptions.builder().applicationType(applicationType).build())

        where:
        applicationType << ApplicationType.values()
    }

    void 'ksp defines documentation'() {
        expect:
        ksp.getFrameworkDocumentation(null)
        ksp.getThirdPartyDocumentation(null)
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
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .testFramework(TestFramework.DEFAULT_OPTION)
                .buildTool(BuildTool.GRADLE_KOTLIN)
                .javaVersion(JdkVersion.JDK_17)
                .build()
        when:
        Map<String, String> output = generate(options)

        then:
        output."gradle.properties" =~ /(?m)^micronautVersion=.+/
        !(output."gradle.properties" =~ /(?m)^org.gradle.jvmargs=.+/)

        where:
        buildTool << BuildTool.valuesGradle()
    }

    void "org.gradle.jvmargs is added for KSP"() {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.KOTLIN)
                .testFramework(TestFramework.DEFAULT_OPTION)
                .buildTool(BuildTool.GRADLE_KOTLIN)
                .javaVersion(JdkVersion.JDK_17)
                .build()
        when:
        Map<String, String> output = generate(options)

        then:
        output."gradle.properties" =~ /(?m)^micronautVersion=.+/
        output."gradle.properties" =~ /(?m)^org.gradle.jvmargs=.+/

        where:
        buildTool << BuildTool.valuesGradle()
    }
}

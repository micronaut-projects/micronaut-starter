package io.micronaut.starter.feature.build

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
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject

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
}

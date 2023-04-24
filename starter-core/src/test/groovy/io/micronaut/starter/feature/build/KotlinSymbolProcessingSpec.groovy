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
}

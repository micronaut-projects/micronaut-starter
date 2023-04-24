package io.micronaut.starter.feature.build

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.LanguageSpecificFeature
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject

class KotlinSymbolProcessingSpec extends ApplicationContextSpec implements CommandOutputFixture {
    @Shared
    @Subject
    KotlinSymbolProcessing ksp = beanContext.getBean(KotlinSymbolProcessing)


    void "ksp requires kotlin"() {
        expect:
        ksp instanceof LanguageSpecificFeature
        ksp.getRequiredLanguage() == Language.KOTLIN
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
}

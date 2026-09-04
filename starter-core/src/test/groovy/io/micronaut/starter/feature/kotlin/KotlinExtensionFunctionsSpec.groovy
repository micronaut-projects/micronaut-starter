package io.micronaut.starter.feature.kotlin

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.BuildBuilder
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.build.BuildTestUtil
import io.micronaut.starter.build.BuildTestVerifier
import io.micronaut.starter.build.dependencies.Scope
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.LanguageSpecificFeature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.util.LanguageUtils
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class KotlinExtensionFunctionsSpec extends ApplicationContextSpec {

    @Subject
    @Shared
    KotlinExtensionFunctions kotlinExtensionFunctions = beanContext.getBean(KotlinExtensionFunctions)

    void "kotlin-extension-functions requires kotlin"() {
        expect:
        kotlinExtensionFunctions instanceof LanguageSpecificFeature
        kotlinExtensionFunctions.getRequiredLanguage() == Language.KOTLIN
    }

    void "kotlin-extension-functions belongs to Logging category"() {
        expect:
        Category.LANGUAGES == kotlinExtensionFunctions.category
    }

    void "kotlin-extension-functions is visible"() {
        expect:
        kotlinExtensionFunctions.visible
    }

    void "kotlin-extension-functions title and description are different"() {
        expect:
        kotlinExtensionFunctions.getTitle()
        kotlinExtensionFunctions.getDescription()
        kotlinExtensionFunctions.getTitle() != kotlinExtensionFunctions.getDescription()
    }

    @Unroll("feature kotlin-extension-functions works for application type: #applicationType")
    void "feature kotlin-extension-functions works for every type of application type"(ApplicationType applicationType) {
        expect:
        kotlinExtensionFunctions.supports(applicationType)

        where:
        applicationType << ApplicationType.values().toList()
    }

    @Ignore("kotlin and maven are no longer supported")
    @Unroll
    void 'dependency is included with maven and feature kotlin-extension-functions for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['kotlin-extension-functions'])
                .render()
        BuildTestVerifier verifier = BuildTestUtil.verifier(BuildTool.MAVEN, language, template)

        then:
        verifier.hasDependency("io.micronaut.kotlin", "micronaut-kotlin-extension-functions", Scope.COMPILE)

        where:
        language << [Language.KOTLIN]
    }

    @Unroll
    void 'exception with maven and feature kotlin-extension-functions for language=#language'(Language language) {
        when:
        new BuildBuilder(beanContext, BuildTool.MAVEN)
                .language(language)
                .features(['kotlin-extension-functions'])
                .render()
        then:
        IllegalArgumentException e = thrown()
        e.message.contains("The selected features are incompatible")

        where:
        language << (supportedLanguages(BuildTool.MAVEN) - Language.KOTLIN)
    }

    @Unroll
    void 'dependency is included with gradle and feature kotlin-extension-functions for language=#language'(Language language) {
        when:
        String template = new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['kotlin-extension-functions'])
                .language(language)
                .render()

        then:
        template.contains('implementation("io.micronaut.kotlin:micronaut-kotlin-extension-functions")')

        where:
        language << [Language.KOTLIN]
    }

    @Unroll
    void 'exception with gradle and feature kotlin-extension-functions for language=#language'(Language language) {
        when:
        new BuildBuilder(beanContext, BuildTool.GRADLE)
                .features(['kotlin-extension-functions'])
                .language(language)
                .render()

        then:
        IllegalArgumentException e = thrown()
        e.message.contains("The selected features are incompatible")

        where:
        language << (LanguageUtils.JVM_LANGUAGES - Language.KOTLIN)
    }
}

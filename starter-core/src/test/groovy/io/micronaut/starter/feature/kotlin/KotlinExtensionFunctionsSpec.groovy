package io.micronaut.starter.feature.kotlin

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.LanguageSpecificFeature
import io.micronaut.starter.options.Language
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

class KotlinExtensionFunctionsSpec extends BeanContextSpec {

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

    @Unroll
    void 'dependency is included with maven and feature kotlin-extension-functions for language=#language'(Language language) {
        when:
        String template = pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['kotlin-extension-functions'], language), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.kotlin</groupId>
      <artifactId>micronaut-kotlin-extension-functions</artifactId>
      <scope>compile</scope>
    </dependency>
""")
        where:
        language << [Language.KOTLIN]
    }

    @Unroll
    void 'exception with maven and feature kotlin-extension-functions for language=#language'(Language language) {
        when:
        pom.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['kotlin-extension-functions'], language), []).render().toString()

        then:
        IllegalArgumentException e = thrown()
        e.message.contains("The selected features are incompatible")

        where:
        language << (Language.values().toList() - Language.KOTLIN)
    }

    @Unroll
    void 'dependency is included with gradle and feature kotlin-extension-functions for language=#language'(Language language) {
        when:
        String template = buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['kotlin-extension-functions'], language), false).render().toString()

        then:
        template.contains('implementation("io.micronaut.kotlin:micronaut-kotlin-extension-functions")')

        where:
        language << [Language.KOTLIN]
    }

    @Unroll
    void 'exception with gradle and feature kotlin-extension-functions for language=#language'(Language language) {
        when:
        buildGradle.template(ApplicationType.DEFAULT, buildProject(), getFeatures(['kotlin-extension-functions'], language), false).render().toString()

        then:
        IllegalArgumentException e = thrown()
        e.message.contains("The selected features are incompatible")

        where:
        language << (Language.values().toList() - Language.KOTLIN)
    }
}

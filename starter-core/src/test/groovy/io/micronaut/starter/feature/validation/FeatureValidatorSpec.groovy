package io.micronaut.starter.feature.validation

import io.micronaut.projectgen.core.feature.FeatureValidator
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.BeanContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.feature.LanguageSpecificFeature
import io.micronaut.projectgen.core.feature.OneOfFeature
import io.micronaut.starter.feature.acme.Acme
import io.micronaut.starter.feature.kotlin.Ktor
import io.micronaut.starter.feature.server.Netty
import io.micronaut.starter.feature.server.ServerFeature
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import spock.lang.Unroll

class FeatureValidatorSpec extends BeanContextSpec {

    FeatureValidator featureValidator = beanContext.getBean(FeatureValidator)

    void "test feature conflicts with language selection"() {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .build()
        when:
        featureValidator.validatePreProcessing(options, [new LanguageSpecificFeature() {
            String name = "test-feature"
            String description = "test desc"
            String title = "test title"
            Language requiredLanguage = Language.GROOVY
        }] as Set)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "The selected features are incompatible. [test-feature] requires groovy but java was the selected language."
    }

    void "test conflicting features required language"() {
        given:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .build()
        when:
        featureValidator.validatePreProcessing(options, [new LanguageSpecificFeature() {
            String name = "groovy-feature"
            String description = "groovy"
            String title = "groovy title"
            Language requiredLanguage = Language.GROOVY
        }, new LanguageSpecificFeature() {
            String name = "kotlin-feature"
            String description = "groovy"
            String title = "groovy title"
            Language requiredLanguage = Language.KOTLIN
        }] as Set)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("The selected features are incompatible")
        ex.message.contains("[groovy-feature] requires groovy")
        ex.message.contains("[kotlin-feature] requires kotlin")
    }

    void "test one of"() {
        when:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(Language.JAVA)
                .build()
        featureValidator.validatePreProcessing(options, [new OneOfFeature() {
            String name = "a"
            String description = "groovy"
            String title = "groovy title"
            Class<?> featureClass = Object.class
        }, new OneOfFeature() {
            String name = "b"
            String description = "groovy"
            String title = "groovy title"
            Class<?> featureClass = Object.class
        }] as Set)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("There can only be one of the following features selected: [a, b]")
    }

    @Unroll
    void "test acme : #serverType"() {
        given:
        def language = serverType instanceof Ktor ? Language.KOTLIN : Language.JAVA
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .language(language)
                .build()

        when:
        featureValidator.validatePreProcessing(options, [
                new Acme(),
                serverType
        ] as Set)

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("Acme only supports Netty")

        where:
        serverType << beanContext.getBeansOfType(ServerFeature).findAll{!(it instanceof Netty)}

    }

}

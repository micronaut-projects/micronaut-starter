package io.micronaut.starter.feature.validation

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.LanguageSpecificFeature
import io.micronaut.starter.feature.OneOfFeature
import io.micronaut.starter.feature.acme.Acme
import io.micronaut.starter.feature.kotlin.Ktor
import io.micronaut.starter.feature.server.Netty
import io.micronaut.starter.feature.server.ServerFeature
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import spock.lang.Unroll

class FeatureValidatorSpec extends BeanContextSpec {

    FeatureValidator featureValidator = beanContext.getBean(FeatureValidator)

    void "test feature conflicts with language selection"() {
        given:
        String featureName = "test-feature"
        Language language = Language.JAVA

        when:
        featureValidator.validatePreProcessing(new Options(language, null, null), ApplicationType.DEFAULT, [new LanguageSpecificFeature() {
            String name = featureName
            String description = "test desc"
            String title = "test title"
            Language requiredLanguage = Language.GROOVY

            @Override
            boolean supports(ApplicationType applicationType) {
                true
            }
        }] as Set)

        then:
        IllegalArgumentException ex = thrown()
        ex.message == "Feature ${featureName} does not support language ${language}. "
    }

    void "test conflicting features required language"() {
        when:
        featureValidator.validatePreProcessing(new Options(Language.JAVA, null, null), ApplicationType.DEFAULT, [new LanguageSpecificFeature() {
            String name = "groovy-feature"
            String description = "groovy"
            String title = "groovy title"
            Language requiredLanguage = Language.GROOVY

            @Override
            boolean supports(ApplicationType applicationType) {
                true
            }
        }, new LanguageSpecificFeature() {
            String name = "kotlin-feature"
            String description = "groovy"
            String title = "groovy title"
            Language requiredLanguage = Language.KOTLIN

            @Override
            boolean supports(ApplicationType applicationType) {
                true
            }
        }] as Set)

        then:
        IllegalArgumentException ex = thrown()
        ex.message.contains("Feature groovy-feature does not support language java. ")
        ex.message.contains("Feature kotlin-feature does not support language java. ")
    }

    void "test one of"() {
        when:
        featureValidator.validatePreProcessing(new Options(Language.JAVA, null, null), ApplicationType.DEFAULT, [new OneOfFeature() {
            String name = "a"
            String description = "groovy"
            String title = "groovy title"
            Class<?> featureClass = Object.class

            @Override
            boolean supports(ApplicationType applicationType) {
                true
            }
        }, new OneOfFeature() {
            String name = "b"
            String description = "groovy"
            String title = "groovy title"
            Class<?> featureClass = Object.class

            @Override
            boolean supports(ApplicationType applicationType) {
                true
            }
        }] as Set)

        then:
        IllegalArgumentException ex = thrown()
        ex.message.contains("There can only be one of the following features selected: [a, b]")
    }

    @Unroll
    void "test acme : #serverType"() {
        given:
        def language = serverType instanceof Ktor ? Language.KOTLIN : Language.JAVA

        when:
        featureValidator.validatePreProcessing(new Options(language, null, null), ApplicationType.DEFAULT, [
                new Acme(),
                serverType
        ] as Set)

        then:
        IllegalArgumentException ex = thrown()
        ex.message.contains("Acme only supports Netty")

        where:
        serverType << beanContext.getBeansOfType(ServerFeature).findAll{!(it instanceof Netty)}

    }

}

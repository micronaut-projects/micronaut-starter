package io.micronaut.starter.feature

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.options.Options
import io.micronaut.starter.feature.validation.FeatureValidator
import io.micronaut.starter.options.Language

class FeatureValidatorSpec extends BeanContextSpec {

    FeatureValidator featureValidator = beanContext.getBean(FeatureValidator)

    void "test feature conflicts with language selection"() {
        when:
        featureValidator.validate(new Options(Language.JAVA, null, null), [new Feature() {
            String name = "test-feature"
            String description = "test desc"
            String title = "test title"
            Optional<Language> requiredLanguage = Optional.of(Language.GROOVY)
        }])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "The selected features are incompatible. [test-feature] requires groovy but java was the selected language."
    }

    void "test conflicting features required language"() {
        when:
        featureValidator.validate(new Options(Language.JAVA, null, null), [new Feature() {
            String name = "groovy-feature"
            String description = "groovy"
            String title = "groovy title"
            Optional<Language> requiredLanguage = Optional.of(Language.GROOVY)
        }, new Feature() {
            String name = "kotlin-feature"
            String description = "groovy"
            String title = "groovy title"
            Optional<Language> requiredLanguage = Optional.of(Language.KOTLIN)
        }])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("The selected features are incompatible")
        ex.message.contains("[groovy-feature] requires groovy")
        ex.message.contains("[kotlin-feature] requires kotlin")
    }

    void "test one of"() {
        when:
        featureValidator.validate(new Options(Language.JAVA, null, null), [new OneOfFeature() {
            String name = "a"
            String description = "groovy"
            String title = "groovy title"
            Class<?> featureClass = Object.class
        }, new OneOfFeature() {
            String name = "b"
            String description = "groovy"
            String title = "groovy title"
            Class<?> featureClass = Object.class
        }])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("There can only be one of the following features selected: [a, b]")
    }

}

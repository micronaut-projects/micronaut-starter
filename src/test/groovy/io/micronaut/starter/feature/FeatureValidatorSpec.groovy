package io.micronaut.starter.feature

import io.micronaut.context.BeanContext
import io.micronaut.starter.options.Language
import spock.lang.Specification
import io.micronaut.starter.feature.validation.FeatureValidator

class FeatureValidatorSpec extends Specification {

    void "test feature conflicts with language selection"() {
        when:
        BeanContext ctx = BeanContext.run()
        FeatureValidator featureValidator = ctx.getBean(FeatureValidator)
        featureValidator.validate(Language.java, [new Feature() {
            String name = "test-feature"
            Optional<Language> requiredLanguage = Optional.of(Language.groovy)
        }])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "The selected features are incompatible. [test-feature] requires groovy but java was the selected language."

        cleanup:
        ctx.close()
    }

    void "test conflicting features required language"() {
        when:
        BeanContext ctx = BeanContext.run()
        FeatureValidator featureValidator = ctx.getBean(FeatureValidator)
        featureValidator.validate(Language.java, [new Feature() {
            String name = "groovy-feature"
            Optional<Language> requiredLanguage = Optional.of(Language.groovy)
        }, new Feature() {
            String name = "kotlin-feature"
            Optional<Language> requiredLanguage = Optional.of(Language.kotlin)
        }])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("The selected features are incompatible")
        ex.message.contains("[groovy-feature] requires groovy")
        ex.message.contains("[kotlin-feature] requires kotlin")

        cleanup:
        ctx.close()
    }

    void "test one of"() {
        when:
        BeanContext ctx = BeanContext.run()
        FeatureValidator featureValidator = ctx.getBean(FeatureValidator)
        featureValidator.validate(Language.java, [new OneOfFeature() {
            String name = "a"
            Class<?> featureClass = Object.class
        }, new OneOfFeature() {
            String name = "b"
            Class<?> featureClass = Object.class
        }])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("There can only be one of the following features selected: [a, b]")

        cleanup:
        ctx.close()
    }

}

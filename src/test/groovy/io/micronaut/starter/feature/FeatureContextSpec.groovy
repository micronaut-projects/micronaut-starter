package io.micronaut.starter.feature

import io.micronaut.starter.command.CreateAppCommand
import io.micronaut.starter.feature.lang.java.Java
import io.micronaut.starter.options.Language
import spock.lang.Specification

class FeatureContextSpec extends Specification {

    void "test the default language is java"() {
        when:
        FeatureContext ctx = new FeatureContext(null, null, null, new CreateAppCommand.CreateAppFeatures(), [])

        then:
        noExceptionThrown()
        ctx.language == Language.java
    }

    void "test language comes from feature required language"() {
        when:
        FeatureContext ctx = new FeatureContext(null, null, null, new CreateAppCommand.CreateAppFeatures(), [new Feature() {
            String name = "test-feature"
            Optional<Language> requiredLanguage = Optional.of(Language.groovy)
        }])

        then:
        noExceptionThrown()
        ctx.language == Language.groovy
    }

    void "test feature conflicts with language selection"() {
        when:
        FeatureContext ctx = new FeatureContext(Language.java, null, null, new CreateAppCommand.CreateAppFeatures(), [new Feature() {
            String name = "test-feature"
            Optional<Language> requiredLanguage = Optional.of(Language.groovy)
        }])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "The selected features are incompatible. [test-feature] requires groovy but java was the selected language."
    }

    void "test conflicting features required language"() {
        when:
        FeatureContext ctx = new FeatureContext(Language.java, null, null, new CreateAppCommand.CreateAppFeatures(), [new Feature() {
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
    }
}

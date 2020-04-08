package io.micronaut.starter.feature

import io.micronaut.context.BeanContext
import io.micronaut.starter.command.CreateAppCommand
import io.micronaut.starter.command.MicronautCommand
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Specification

class FeatureContextSpec extends Specification {

    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    void "test the default language is java"() {
        when:
                FeatureContext ctx = new FeatureContext(null, null, null, MicronautCommand.CREATE_APP, beanContext.getBean(CreateAppCommand.CreateAppFeatures), [])

        then:
        noExceptionThrown()
        ctx.language == Language.java
    }

    void "test language comes from feature required language"() {
        when:
        FeatureContext ctx = new FeatureContext(null, null, null, MicronautCommand.CREATE_APP, beanContext.getBean(CreateAppCommand.CreateAppFeatures), [new Feature() {
            String name = "test-feature"
            Optional<Language> requiredLanguage = Optional.of(Language.groovy)
        }])

        then:
        noExceptionThrown()
        ctx.language == Language.groovy
    }

}

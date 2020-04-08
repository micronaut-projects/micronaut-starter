package io.micronaut.starter.feature

import io.micronaut.context.BeanContext
import io.micronaut.starter.command.CreateAppCommand
import io.micronaut.starter.command.MicronautCommand
import io.micronaut.starter.feature.micrometer.AppOptics
import io.micronaut.starter.feature.micrometer.Core
import io.micronaut.starter.feature.other.Management
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

    void 'test mandatory dependencies for micrometer features are added'() {
        given: 'a new feature context with one micrometer feature'
        FeatureContext ctx = new FeatureContext(null, null, null, MicronautCommand.CREATE_APP, beanContext.getBean(CreateAppCommand.CreateAppFeatures),
                                                [new AppOptics(new Core(), new Management())])

        when: 'processing all the features'
        ctx.processSelectedFeatures()

        then: 'the micrometer feature is added and also micrommeter-core and management'
        ctx.features.collect { it.class == AppOptics }
        ctx.features.collect { it.class == Core }
        ctx.features.collect { it.class == Management }
    }

}

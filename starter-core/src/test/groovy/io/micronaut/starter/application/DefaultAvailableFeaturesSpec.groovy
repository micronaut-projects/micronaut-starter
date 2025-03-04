package io.micronaut.starter.application

import io.micronaut.context.ApplicationContext
import spock.lang.Specification

class DefaultAvailableFeaturesSpec extends Specification {
    void "bean of type DefaultAvailableFeatures"() {
        given:
        ApplicationContext ctx = ApplicationContext.run()

        when:
        ctx.getBean(DefaultAvailableFeatures)

        then:
        noExceptionThrown()

        cleanup:
        ctx.close()
    }
}

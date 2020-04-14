package io.micronaut.starter.feature.discovery

import io.micronaut.context.BeanContext
import io.micronaut.starter.fixture.ContextFixture
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class DiscoverySpec extends Specification implements ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    void 'test there can only be one discovery feature'() {
        when:
        getFeatures(["discovery-consul", "discovery-eureka"])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("There can only be one of the following features selected")
    }

}

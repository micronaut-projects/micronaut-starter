package io.micronaut.starter.generator.app

import io.micronaut.context.BeanContext
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class RandomPortSpec extends Specification {
    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    void "random-port feature exists"() {
        expect:
        beanContext.containsBean(RandomPort)
    }
}

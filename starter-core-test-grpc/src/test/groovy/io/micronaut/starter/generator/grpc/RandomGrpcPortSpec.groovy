package io.micronaut.starter.generator.grpc

import io.micronaut.context.BeanContext
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class RandomGrpcPortSpec extends Specification {
    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    void "random-grpc-port feature exists"() {
        expect:
        beanContext.containsBean(RandomGrpcPort)
    }
}

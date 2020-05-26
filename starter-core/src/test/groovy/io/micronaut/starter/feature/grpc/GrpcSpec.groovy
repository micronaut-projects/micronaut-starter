package io.micronaut.starter.feature.grpc

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.feature.Category
import spock.lang.Shared
import spock.lang.Subject

class GrpcSpec extends BeanContextSpec {

    @Shared
    @Subject
    Grpc grpc = beanContext.getBean(Grpc)

    void "grpc belongs to API category"() {
        expect:
        Category.API == grpc.category
    }
}

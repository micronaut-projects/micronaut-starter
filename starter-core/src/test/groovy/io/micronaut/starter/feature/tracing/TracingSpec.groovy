package io.micronaut.starter.feature.tracing

import io.micronaut.context.BeanContext
import io.micronaut.starter.fixture.ContextFixture
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class TracingSpec extends Specification implements ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    void 'test there can only be one tracing feature'() {
        when:
        getFeatures(["tracing-zipkin", "tracing-jaeger"])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("There can only be one of the following features selected")
    }

}

package io.micronaut.starter.feature.tracing

import io.micronaut.starter.BeanContextSpec

class TracingSpec extends BeanContextSpec {

    void 'test there can only be one tracing feature'() {
        when:
        getFeatures(['tracing-zipkin', 'tracing-jaeger'])

        then:
        IllegalArgumentException e = thrown()
        e.message.contains 'There can only be one of the following features selected'
    }
}

package io.micronaut.starter.feature.discovery

import io.micronaut.starter.BeanContextSpec

class DiscoverySpec extends BeanContextSpec {

    void 'test there can only be one discovery feature'() {
        when:
        getFeatures(["discovery-consul", "discovery-eureka"])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("There can only be one of the following features selected")
    }

}

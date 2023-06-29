package io.micronaut.starter.feature.cache

import io.micronaut.starter.BeanContextSpec
import spock.lang.Unroll

class CacheSpec extends BeanContextSpec {

    void 'test there can only be one cache feature'() {
        when:
        getFeatures(["cache-ehcache", "cache-hazelcast", "cache-infinispan", "cache-coherence"])

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message.contains("There can only be one of the following features selected")
    }

    @Unroll
    void 'cache-caffeine can be selected with #otherCache feature'() {
        when:
        getFeatures(["cache-caffeine", otherCache])

        then:
        noExceptionThrown()

        where:
        otherCache << ["cache-ehcache", "cache-hazelcast", "cache-infinispan", "cache-coherence"]
    }

}

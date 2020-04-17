package io.micronaut.starter.feature.cache

import io.micronaut.context.BeanContext
import io.micronaut.starter.fixture.ContextFixture
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class CacheSpec extends Specification implements ContextFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    void 'test there can only be one cache feature'() {
        when:
        getFeatures(["cache-ehcache", "cache-hazelcast", "cache-infinispan"])

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
        otherCache << ["cache-ehcache", "cache-hazelcast", "cache-infinispan"]
    }

}

package io.micronaut.starter.feature

import io.micronaut.context.BeanContext
import io.micronaut.starter.feature.micrometer.AppOptics
import io.micronaut.starter.feature.micrometer.Core
import io.micronaut.starter.feature.other.Management
import io.micronaut.starter.fixture.FeatureContextFixture
import spock.lang.AutoCleanup
import spock.lang.Specification

class FeatureContextSpec extends Specification implements FeatureContextFixture {

    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    void 'test mandatory dependencies for micrometer features are added'() {
        when: 'a new feature context with one micrometer feature'
        FeatureContext ctx = buildAndProcess(["micrometer-appoptics"])

        then: 'the micrometer feature is added and also micrommeter-core and management'
        ctx.features*.name.containsAll(['micrometer-appoptics', 'micrometer', 'management'])
    }

}

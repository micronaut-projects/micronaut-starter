package io.micronaut.starter.feature.other

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture
import spock.lang.Unroll

class ManagementSpec extends BeanContextSpec implements CommandOutputFixture {

    @Unroll
    void 'test readme.md contains links to hazelcast and micronaut docs'() {
        when:
        def output = generate(['management'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#management")
    }
}

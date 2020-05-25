package io.micronaut.starter.feature.reloading

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture

class JrebelSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature jrebel contains links to micronaut docs'() {
        when:
        def output = generate(['jrebel'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#jrebel")
    }
}

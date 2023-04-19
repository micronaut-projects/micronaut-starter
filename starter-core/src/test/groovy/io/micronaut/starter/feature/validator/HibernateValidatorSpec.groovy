package io.micronaut.starter.feature.validator

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture

class HibernateValidatorSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md contains links to micronaut docs'() {
        when:
        def output = generate(['hibernate-validator'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-hibernate-validator/latest/guide/index.html")
    }
}

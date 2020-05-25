package io.micronaut.starter.feature.server

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture

class JettyTestSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature jetty-server contains links to micronaut docs'() {
        when:
        def output = generate(['jetty-server'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-servlet/1.0.x/guide/index.html#jetty")
    }

}

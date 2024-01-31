package io.micronaut.starter.feature.server

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture

class UndertowSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature undertow-server contains links to micronaut docs'() {
        when:
        Map<String, String> output = generate(['undertow-server'])
        String readme = output["README.md"]

        then:
        readme
        readme.contains("https://micronaut-projects.github.io/micronaut-servlet/1.0.x/guide/index.html#undertow")
    }

}

package io.micronaut.starter.feature.other

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture

class HttpClientSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test readme.md with feature http-client contains links to micronaut docs'() {
        when:
        def output = generate(['http-client'])
        def readme = output["README.md"]

        then:
        readme
        readme.contains("https://docs.micronaut.io/latest/guide/index.html#httpClient")
    }
}

package io.micronaut.starter.feature.httpclient

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture

class HttpClientFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'test only one of the HttpClient features can be selected'() {
        when:
        generate(['http-client', 'http-client-jdk'])

        then:
        def e = thrown(IllegalArgumentException)
        e.message.startsWith("There can only be one of the following features selected:")
    }
}

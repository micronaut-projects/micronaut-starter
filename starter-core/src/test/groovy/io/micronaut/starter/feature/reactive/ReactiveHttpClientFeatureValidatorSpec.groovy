package io.micronaut.starter.feature.reactive

import io.micronaut.starter.BeanContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.options.Options

class ReactiveHttpClientFeatureValidatorSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test third part server validation fails with micronaut server features'() {
        when:
        Options options = new Options()
        generate(ApplicationType.DEFAULT, options, ['reactor-http-client', 'http-client-jdk'])

        then:
        IllegalArgumentException e = thrown()
        e.message.startsWith("http-client-jdk feature is not compatible with a reactive HTTP Client")
    }
}

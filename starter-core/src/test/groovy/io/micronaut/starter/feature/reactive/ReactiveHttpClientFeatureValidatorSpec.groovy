package io.micronaut.starter.feature.reactive

import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.BeanContextSpec
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.projectgen.core.options.Options

class ReactiveHttpClientFeatureValidatorSpec extends BeanContextSpec  implements CommandOutputFixture {

    void 'test third part server validation fails with micronaut server features'() {
        when:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.DEFAULT)
                .features(['reactor-http-client', 'http-client-jdk'])
                .build()
        generate(options)

        then:
        IllegalArgumentException e = thrown()
        e.message.startsWith("http-client-jdk feature is not compatible with a reactive HTTP Client")
    }
}

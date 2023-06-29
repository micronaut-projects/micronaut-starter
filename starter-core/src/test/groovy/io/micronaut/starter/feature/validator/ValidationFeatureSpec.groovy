package io.micronaut.starter.feature.validator

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.fixture.CommandOutputFixture

class ValidationFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture{

    void 'test only one of the validation features can be selected'() {
        when:
        generate(['hibernate-validator', 'validation'])

        then:
        def e = thrown(IllegalArgumentException)
        e.message.startsWith("There can only be one of the following features selected:")
    }
}

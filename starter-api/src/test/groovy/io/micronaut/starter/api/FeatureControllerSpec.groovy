package io.micronaut.starter.api

import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class FeatureControllerSpec extends Specification {
    @Inject
    ApplicationTypeClient client

    void "test list features"() {
        when:
        List<FeatureDTO> features = client.features(ApplicationTypes.app)

        then:
        !features.isEmpty()
    }

    void "test list features for application type"() {
        when:
        def features = client.features(ApplicationTypes.cli)

        then:
        !features.any { it.name == 'swagger' }

        when:
        features = client.features(ApplicationTypes.app)

        then:
        features.any { it.name == 'swagger' }
    }

    @Client('/')
    static interface ApplicationTypeClient extends ApplicationTypeOperations {}
}

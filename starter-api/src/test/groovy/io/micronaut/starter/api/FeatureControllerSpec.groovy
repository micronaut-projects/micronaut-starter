package io.micronaut.starter.api

import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class FeatureControllerSpec extends Specification {
    @Inject
    FeatureClient client

    void "test list features"() {
        when:
        List<FeatureDTO> features = client.features()

        then:
        !features.isEmpty()
    }

    @Client('/features')
    static interface FeatureClient extends FeatureOperations {}
}

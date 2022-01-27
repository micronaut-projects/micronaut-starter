package io.micronaut.starter.api

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class FeatureOperationsSpec extends Specification {

    @Inject
    FeatureOperations featureOperations

    void "only visble features are exposed"() {
        when:
        List<FeatureDTO> features = featureOperations.getAllFeatures(Locale.ENGLISH)

        then:
        features
        features.any {it.name == 'gradle-enterprise' }
        features.stream().noneMatch(feature -> feature.name == 'micronaut-gradle-enterprise' )
    }
}

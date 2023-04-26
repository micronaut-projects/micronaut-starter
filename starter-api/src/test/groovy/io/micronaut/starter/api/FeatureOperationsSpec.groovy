package io.micronaut.starter.api

import io.micronaut.starter.feature.build.MicronautGradleEnterprise
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class FeatureOperationsSpec extends Specification {

    @Inject
    FeatureOperations featureOperations

    void "only visible features are exposed"() {
        when:
        List<FeatureDTO> features = featureOperations.getAllFeatures(Locale.ENGLISH)

        then:
        features
        features.stream().noneMatch(feature -> feature.name == MicronautGradleEnterprise.NAME )
    }
}

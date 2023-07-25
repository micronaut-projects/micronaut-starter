package io.micronaut.starter.api

import io.micronaut.context.i18n.ResourceBundleMessageSource
import io.micronaut.http.HttpHeaders
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.annotation.Client
import io.micronaut.starter.application.ApplicationType
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import spock.lang.Specification

@MicronautTest
class FeatureControllerSpec extends Specification {

    @Inject
    ApplicationTypeClient client

    void "test list features"() {
        when:
        List<FeatureDTO> features = client.features(ApplicationType.DEFAULT, RequestInfo.LOCAL).features

        then:
        !features.isEmpty()
    }

    void "test community features"() {
        when:
        List<FeatureDTO> communityFeatures = client.features(ApplicationType.DEFAULT, RequestInfo.LOCAL).features.findAll { it.community }

        then:
        communityFeatures.name.sort() == [
                'camunda-platform7',
                'camunda-external-worker',
                'agorapulse-gru-http',
                'agorapulse-micronaut-console',
                'agorapulse-micronaut-permissions',
                'agorapulse-micronaut-slack',
                'agorapulse-micronaut-worker',
                'camunda-zeebe',
                'jobrunr-jobrunr'
        ].sort()
    }

    void "test list features - spanish"() {
        when:
        List<FeatureDTO> features = client.spanishFeatures(ApplicationType.DEFAULT).features
        def graal = features.find { it.name == 'graalvm' }

        then:
        graal.description == 'crear aplicaciones nativas'
        !graal.isPreview()
        !graal.isCommunity()
    }

    void "test list features for application type"() {
        when:
        def features = client.features(ApplicationType.CLI, RequestInfo.LOCAL).features

        then:
        !features.any { it.name == 'openapi' }

        when:
        features = client.features(ApplicationType.DEFAULT, RequestInfo.LOCAL).features

        then:
        features.any { it.name == 'openapi' }
    }

    @Client('/')
    static interface ApplicationTypeClient extends ApplicationTypeOperations {
        @Get("/application-types/{type}/features")
        @Header(name = HttpHeaders.ACCEPT_LANGUAGE, value = "es")
        FeatureList spanishFeatures(ApplicationType type);
    }

    @Singleton
    static class Spanish extends ResourceBundleMessageSource {

        Spanish() {
            super("features", new Locale("es"))
        }
    }
}

package io.micronaut.starter.api

import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class ApplicationControllerSpec extends Specification {

    @Inject
    @Client("/")
    RxHttpClient client

    @Inject
    ApplicationTypeClient applicationTypeClient

    void "test versions"() {
        given:
        def response = client.retrieve(HttpRequest.GET('/versions'), Map).blockingFirst()

        expect:
        response.containsKey("versions")
        response.versions["micronaut.version"]
    }

    void "test application types"() {
        when:
        def types = applicationTypeClient.spanishTypes()

        then:
        types.types.find { it.name == 'cli' }.description == 'crear aplicaciones para la linea de commandos'
    }

    @Client('/')
    static interface ApplicationTypeClient extends ApplicationTypeOperations {
        @Get("/application-types")
        @Header(name = HttpHeaders.ACCEPT_LANGUAGE, value = "es")
        ApplicationTypeList spanishTypes();
    }
}

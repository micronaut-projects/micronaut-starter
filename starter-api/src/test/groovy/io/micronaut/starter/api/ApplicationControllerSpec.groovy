package io.micronaut.starter.api

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.MicronautTest
import io.reactivex.Flowable
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest
class ApplicationControllerSpec extends Specification {

    @Inject
    @Client("/")
    RxHttpClient client

    void "test versions"() {
        given:
        def response = client.retrieve(HttpRequest.GET('/versions'), Map).blockingFirst()

        expect:
        response.containsKey("versions")
        response.versions["micronaut.version"]
    }
}

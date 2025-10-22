package io.micronaut.starter.api

import io.micronaut.context.annotation.Property
import io.micronaut.core.util.StringUtils
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@MicronautTest
class ApplicationControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client

    @Inject
    ApplicationTypeClient applicationTypeClient

    void "home txt request"() {
        when:
        MutableHttpRequest<?> mutableHttpRequest = HttpRequest.GET("/").accept(MediaType.TEXT_PLAIN_TYPE)
        HttpResponse<?> response = client.toBlocking().exchange(mutableHttpRequest)

        then:
        noExceptionThrown()
        HttpStatus.OK == response.status()

        when:
        mutableHttpRequest = HttpRequest.GET("/")
        response = client.toBlocking().exchange(mutableHttpRequest)

        then:
        noExceptionThrown()
        HttpStatus.OK == response.status()
    }

    void "htmlRequest redirects"() {
        given:
        MutableHttpRequest<?> mutableHttpRequest = HttpRequest.GET("/");
        mutableHttpRequest.getHeaders().add("Accept", "text/html,application/xhtml xml,application/xml;q=0.9,*/*;q=0.8")
        mutableHttpRequest.getHeaders().add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/17.6 Safari/605.1.15")
        mutableHttpRequest.getHeaders().add("Accept-Encoding", "gzip, deflate")
        mutableHttpRequest.getHeaders().add("Accept-Language", "en-GB,en;q=0.9")
        mutableHttpRequest.getHeaders().add("Sec-Fetch-Mode", "navigate")
        mutableHttpRequest.getHeaders().add("Upgrade-Insecure-Requests", "1")
        mutableHttpRequest.getHeaders().add("Sec-Fetch-Dest", "document")
        mutableHttpRequest.getHeaders().add("Host", "localhost:8080");

        when:
        HttpResponse<?> response = client.toBlocking().exchange(mutableHttpRequest)

        then:
        HttpStatus.PERMANENT_REDIRECT == response.status()
    }

    void "test versions"() {
        given:
        def response = client.toBlocking().retrieve(HttpRequest.GET('/versions'), Map)

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

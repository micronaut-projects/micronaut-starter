package io.micronaut.starter.api.options

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.starter.api.JdkVersionDTO
import io.micronaut.starter.api.SelectOptionsDTO
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class SelectOptionsControllerTest extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient

    void "Only JDK 17 is supported"() {
        BlockingHttpClient client = httpClient.toBlocking()

        HttpRequest<?> request = HttpRequest.GET("/select-options")
        when:
        SelectOptionsDTO selectOptionsDTO = client.retrieve(request, SelectOptionsDTO)

        then:
        noExceptionThrown()
        ['JDK_17'] == selectOptionsDTO.jdkVersion.options.stream().map(JdkVersionDTO::getName).toList()
    }
}

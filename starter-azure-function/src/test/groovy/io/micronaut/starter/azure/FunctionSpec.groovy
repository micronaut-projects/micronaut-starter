package io.micronaut.starter.azure

import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpStatus
import spock.lang.Specification

import java.util.zip.ZipInputStream

class FunctionSpec extends Specification {

    void "test list features"() {

        when:
        def function = new Function()
        def response = function.request(HttpMethod.GET, "/features")
                                .invoke()
        then:
        response.status.value() == HttpStatus.OK.code
    }

    void "test create app"() {

        when:
        def function = new Function()
        def response = function.request(HttpMethod.GET, "/create/app/test")
                .invoke()
        def zipInputStream = new ZipInputStream(new ByteArrayInputStream(response.body))
        def zipEntry = zipInputStream.getNextEntry()
        then:
        response.status.value() == HttpStatus.CREATED.code
        zipEntry != null
        response.getHeader(HttpHeaders.CONTENT_DISPOSITION).contains("application.zip")
    }
}

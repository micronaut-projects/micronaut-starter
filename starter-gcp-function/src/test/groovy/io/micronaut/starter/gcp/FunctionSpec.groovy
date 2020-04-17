package io.micronaut.starter.gcp

import io.micronaut.gcp.function.http.HttpFunction
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpStatus
import spock.lang.Specification

class FunctionSpec extends Specification {

    void "test list features"() {

        when:
        def function = new HttpFunction()
        def response = function.invoke(HttpMethod.GET, "/api/features")
        then:
        response.status == HttpStatus.OK
    }

    void "test create app"() {

        when:
        def function = new HttpFunction()
        def response = function.invoke(HttpMethod.GET, "/api/create/app/test")
        then:
        response.status == HttpStatus.CREATED
        response.httpHeaders.get(HttpHeaders.CONTENT_DISPOSITION).contains("application.zip")
    }
}


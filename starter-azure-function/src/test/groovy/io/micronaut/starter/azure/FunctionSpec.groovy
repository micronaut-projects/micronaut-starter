package io.micronaut.starter.azure

import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpStatus
import spock.lang.Specification

class FunctionSpec extends Specification {

    void "test azure function execution"() {

        when:
        def function = new Function()
        def response = function.request(HttpMethod.GET, "/features")
                                .invoke()
        then:
        response.status.value() == HttpStatus.OK.code
    }
}

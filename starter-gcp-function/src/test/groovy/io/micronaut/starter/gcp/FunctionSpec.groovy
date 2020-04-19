package io.micronaut.starter.gcp

import io.micronaut.core.type.Argument
import io.micronaut.gcp.function.http.HttpFunction
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpStatus
import io.micronaut.starter.util.ZipUtil
import spock.lang.Specification

class FunctionSpec extends Specification {

    void "test list features"() {

        when:
        def function = new HttpFunction()
        def response = function.invoke(HttpMethod.GET, '/application-types/app/features')
        then:
        response.status == HttpStatus.OK
    }

    void "test create app"() {

        when:
        def function = new HttpFunction()
        def response = function.invoke(HttpMethod.GET, "/create/app/test")
        byte[] bytes = response.getBody(Argument.of(byte[].class)).get()

        then:
        response.status == HttpStatus.CREATED
        ZipUtil.isZip(bytes)
        response.httpHeaders.get(HttpHeaders.CONTENT_DISPOSITION).contains("application.zip")
    }
}


package io.micronaut.starter.gcp

import io.micronaut.core.type.Argument
import io.micronaut.gcp.function.http.GoogleHttpResponse
import io.micronaut.gcp.function.http.HttpFunction
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpStatus
import io.micronaut.starter.util.ZipUtil
import spock.lang.Requires
import spock.lang.Specification

@Requires({ jvm.current.isJava11Compatible() })
class FunctionSpec extends Specification {

    void "test list features"() {

        when:
        def function = new HttpFunction()
        def response = function.invoke(HttpMethod.GET, '/application-types/default/features')
        then:
        response.status == HttpStatus.OK
    }

    void "test create app"() {
        when:
        HttpFunction function = new HttpFunction()
        GoogleHttpResponse response = function.invoke(HttpMethod.GET, "/create/default/test")

        then:
        response.status == HttpStatus.CREATED

        when:
        byte[] bytes = response.getBody(Argument.of(byte[].class)).get()

        then:
        ZipUtil.isZip(bytes)
        response.httpHeaders.get(HttpHeaders.CONTENT_DISPOSITION).contains("test.zip")
    }
}


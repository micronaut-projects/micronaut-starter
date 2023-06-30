package io.micronaut.starter.azure

import com.microsoft.azure.functions.HttpResponseMessage
import io.micronaut.azure.function.http.AzureHttpFunction
import io.micronaut.azure.function.http.DefaultExecutionContext
import io.micronaut.azure.function.http.HttpRequestMessageBuilder
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpStatus
import io.micronaut.starter.util.ZipUtil
import spock.lang.Specification

class FunctionSpec extends Specification {

    void "test list features"() {
        given:
        Function function = new Function()

        when:
        HttpResponseMessage responseMessage = invoke(function, function.request(HttpMethod.GET, '/application-types/default/features'))

        then:
        responseMessage.status.value() == HttpStatus.OK.code
    }

    void "test create app"() {
        given:
        Function function = new Function()

        when:
        HttpResponseMessage response = invoke(function, function.request(HttpMethod.GET, "/create/default/test"))

        then:
        response.status.value() == HttpStatus.CREATED.code
        response.getHeader(HttpHeaders.CONTENT_DISPOSITION).contains("test.zip")
        ZipUtil.isZip(response.body as byte[])
    }

    static HttpResponseMessage invoke(AzureHttpFunction function, HttpRequestMessageBuilder<?> builder) {
        function.route(
                builder.buildEncoded(),
                new DefaultExecutionContext()
        );
    }
}

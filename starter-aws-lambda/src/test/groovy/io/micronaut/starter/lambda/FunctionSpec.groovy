package io.micronaut.starter.lambda

import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext
import com.amazonaws.serverless.proxy.model.AwsProxyRequest
import com.amazonaws.serverless.proxy.model.AwsProxyResponse
import com.amazonaws.services.lambda.runtime.Context
import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.function.aws.proxy.MicronautLambdaHandler
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpStatus
import io.micronaut.starter.api.FeatureList
import io.micronaut.starter.util.ZipUtil
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import java.nio.charset.StandardCharsets

class FunctionSpec extends Specification {
    @Shared
    @AutoCleanup
    MicronautLambdaHandler handler = new MicronautLambdaHandler()

    @Shared
    ObjectMapper objectMapper = handler.applicationContext.getBean(ObjectMapper)

    @Shared Context lambdaContext = new MockLambdaContext()


    void "test list features"() {
        when:
        AwsProxyRequest request = new AwsProxyRequestBuilder('/application-types/default/features', HttpMethod.GET.toString())
                .build()
        AwsProxyResponse response = handler.handleRequest(request, lambdaContext)

        then:
        response.statusCode == HttpStatus.OK.code
        response.body

        when:
        FeatureList featureList = objectMapper.readValue(response.body, FeatureList)

        then:
        !featureList.features.isEmpty()
    }

    void "test create app"() {
        when:
        AwsProxyRequest request = new AwsProxyRequestBuilder('/create/default/test', HttpMethod.GET.toString()).build()
        AwsProxyResponse response = handler.handleRequest(request, lambdaContext)
        byte[] bytes = response.body.getBytes(StandardCharsets.UTF_8)

        then:
        response.statusCode == HttpStatus.CREATED.code
        ZipUtil.isZip(Base64.mimeDecoder.decode(bytes))
        response.multiValueHeaders.getFirst(HttpHeaders.CONTENT_DISPOSITION).contains("test.zip")
    }
}

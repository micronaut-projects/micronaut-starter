package io.micronaut.starter.lambda

import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext
import com.amazonaws.services.lambda.runtime.Context
import io.micronaut.context.ApplicationContext
import io.micronaut.function.aws.proxy.MicronautLambdaContainerHandler
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpStatus
import io.micronaut.starter.util.ZipUtil
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import java.nio.charset.StandardCharsets

class FunctionSpec extends Specification {
    @Shared @AutoCleanup MicronautLambdaContainerHandler handler = new MicronautLambdaContainerHandler(
            ApplicationContext.build()
    )
    @Shared Context lambdaContext = new MockLambdaContext()


    void "test list features"() {

        when:
        AwsProxyRequestBuilder builder = new AwsProxyRequestBuilder('/application-types/app/features', HttpMethod.GET.toString())
        def response = handler.proxy(builder.build(), lambdaContext)

        then:
        response.statusCode == HttpStatus.OK.code
    }

    void "test create app"() {

        when:
        AwsProxyRequestBuilder builder = new AwsProxyRequestBuilder('/create/app/test', HttpMethod.GET.toString())
        def response = handler.proxy(builder.build(), lambdaContext)
        def bytes = response.body.getBytes(StandardCharsets.UTF_8)

        then:
        response.statusCode == HttpStatus.CREATED.code
        ZipUtil.isZip(bytes)
        response.multiValueHeaders.getFirst(HttpHeaders.CONTENT_DISPOSITION).contains("application.zip")
    }
}

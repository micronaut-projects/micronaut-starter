package io.micronaut.starter.lambda

import com.amazonaws.services.lambda.runtime.ClientContext
import com.amazonaws.services.lambda.runtime.CognitoIdentity
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.LambdaLogger
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import io.micronaut.core.type.Argument
import io.micronaut.function.aws.proxy.payload1.ApiGatewayProxyRequestEventFunction
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpMethod
import io.micronaut.http.HttpStatus
import io.micronaut.json.JsonMapper
import io.micronaut.starter.api.FeatureList
import io.micronaut.starter.util.ZipUtil
import spock.lang.AutoCleanup
import spock.lang.PendingFeature
import spock.lang.Shared
import spock.lang.Specification

import java.nio.charset.StandardCharsets

class FunctionSpec extends Specification {

    @Shared
    @AutoCleanup
    ApiGatewayProxyRequestEventFunction handler = new ApiGatewayProxyRequestEventFunction()

    static final Context lambdaContext = new Context() {

        @Override
        String getAwsRequestId() {
            return null
        }

        @Override
        String getLogGroupName() {
            return null
        }

        @Override
        String getLogStreamName() {
            return null
        }

        @Override
        String getFunctionName() {
            return null
        }

        @Override
        String getFunctionVersion() {
            return null
        }

        @Override
        String getInvokedFunctionArn() {
            return null
        }

        @Override
        CognitoIdentity getIdentity() {
            return null
        }

        @Override
        ClientContext getClientContext() {
            return null
        }

        @Override
        int getRemainingTimeInMillis() {
            return 0
        }

        @Override
        int getMemoryLimitInMB() {
            return 0
        }

        @Override
        LambdaLogger getLogger() {
            return null
        }
    }
    void "test list features"() {
        when:
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent();
        request.setPath('/application-types/default/features')
        request.setHttpMethod(HttpMethod.GET.toString())

        APIGatewayProxyResponseEvent response = handler.handleRequest(request, lambdaContext)

        then:
        response.statusCode == HttpStatus.OK.code
        response.body

        when:
        JsonMapper jsonMapper = handler.getApplicationContext().getBean(JsonMapper.class)
        FeatureList featureList = jsonMapper.readValue(response.body, Argument.of(FeatureList.class))

        then:
        !featureList.features.isEmpty()
    }

    @PendingFeature
    void "test create app"() {
        when:
        APIGatewayProxyRequestEvent request = new APIGatewayProxyRequestEvent()
        request.setPath('/create/default/test')
        request.setHttpMethod(HttpMethod.GET.toString())
        APIGatewayProxyResponseEvent response = handler.handleRequest(request, lambdaContext)

        then:
        response.statusCode == HttpStatus.CREATED.code
        response.multiValueHeaders.get(HttpHeaders.CONTENT_DISPOSITION).stream().anyMatch {it.contains("test.zip") }

        when:
        byte[] bytes = response.body.getBytes(StandardCharsets.UTF_8)
        boolean isZip = ZipUtil.isZip(Base64.mimeDecoder.decode(bytes))

        then:
        noExceptionThrown()
        isZip
    }
}

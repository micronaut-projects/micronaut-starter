package io.micronaut.starter.feature.aws

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import spock.lang.Subject

class LambdaFunctionUrlSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    LambdaFunctionUrl lambdaFunctionUrl = beanContext.getBean(LambdaFunctionUrl)

    void 'aws-lambda-function-url feature is in the cloud category'() {
        expect:
        lambdaFunctionUrl.category == Category.CLOUD
    }

    void "aws-lambda-function-url does not support #applicationType application type"(ApplicationType applicationType) {
        expect:
        !lambdaFunctionUrl.supports(applicationType)

        where:
        applicationType << (ApplicationType.values() - ApplicationType.FUNCTION)
    }

    void "aws-lambda-function-url supports function application type"() {
        expect:
        lambdaFunctionUrl.supports(ApplicationType.FUNCTION)
    }
}

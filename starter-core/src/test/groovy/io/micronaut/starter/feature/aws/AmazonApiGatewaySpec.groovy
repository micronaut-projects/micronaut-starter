package io.micronaut.starter.feature.aws

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import spock.lang.Subject

class AmazonApiGatewaySpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    AmazonApiGateway amazonApiGateway = beanContext.getBean(AmazonApiGateway)

    void 'amazon-api-gateway feature is in the cloud category'() {
        expect:
        amazonApiGateway.category == Category.CLOUD
    }

    void 'amazon-api-gateway feature is an instance of AwsApiFeature'() {
        expect:
        amazonApiGateway instanceof AwsApiFeature
        amazonApiGateway instanceof LambdaTrigger
    }

    void "amazon-api-gateway does not support #applicationType application type"(ApplicationType applicationType) {
        expect:
        !amazonApiGateway.supports(applicationType)

        where:
        applicationType << (ApplicationType.values() - ApplicationType.FUNCTION - ApplicationType.DEFAULT)
    }

    void "amazon-api-gateway supports function application type"() {
        expect:
        amazonApiGateway.supports(ApplicationType.FUNCTION)
        amazonApiGateway.supports(ApplicationType.DEFAULT)
    }
}

package io.micronaut.starter.feature.aws

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
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

    void 'amazon-api-gateway feature with Cdk has dependency in infra and doc links'() {
        when:
        def output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, BuildTool.GRADLE),
                [Cdk.NAME, AmazonApiGateway.NAME])

        then:
        output."$Cdk.INFRA_MODULE/build.gradle".contains($/implementation("io.micronaut.aws:micronaut-aws-apigateway/$)
        output."README.md".contains($/https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#amazonApiGateway/$)
        output."README.md".contains($/https://docs.aws.amazon.com/apigateway/$)
    }

    void 'amazon-api-gateway feature without Cdk has dependency in project and doc links'() {
        when:
        def output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, BuildTool.GRADLE),
                [AmazonApiGateway.NAME])

        then:
        output."build.gradle".contains($/implementation("io.micronaut.aws:micronaut-aws-apigateway/$)
        output."README.md".contains($/https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#amazonApiGateway/$)
        output."README.md".contains($/https://docs.aws.amazon.com/apigateway/$)

    }
}

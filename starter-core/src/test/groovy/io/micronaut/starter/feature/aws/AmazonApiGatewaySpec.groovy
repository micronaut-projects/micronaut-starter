package io.micronaut.starter.feature.aws

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject

class AmazonApiGatewaySpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    AmazonApiGateway amazonApiGateway = beanContext.getBean(AmazonApiGateway)

    @Shared
    Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, AwsLambdaFeatureValidator.firstSupportedJdk())

    void 'amazon-api-gateway feature is in the cloud category'() {
        expect:
        amazonApiGateway.category == Category.SERVERLESS
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

    void 'amazon-api-gateway feature with Cdk has doc links'() {
        when:
        def output = generate(ApplicationType.FUNCTION, options,
                [Cdk.NAME, AmazonApiGateway.NAME])

        then:
        output."README.md".contains($/https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#amazonApiGateway/$)
        output."README.md".contains($/https://docs.aws.amazon.com/apigateway/$)
    }

    void 'amazon-api-gateway feature without Cdk has doc links'() {
        when:
        def output = generate(ApplicationType.FUNCTION, options,
                [AmazonApiGateway.NAME])

        then:
        output."README.md".contains($/https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#amazonApiGateway/$)
        output."README.md".contains($/https://docs.aws.amazon.com/apigateway/$)

    }

    void 'amazon-api-gateway feature is a OneOfFeature with amazon-api-gateway-http'() {
        when:
        def output = generate(ApplicationType.FUNCTION, options,
                [AmazonApiGateway.NAME, AmazonApiGatewayHttp.NAME])

        then:
        def ex = thrown IllegalArgumentException
        ex.message.contains('There can only be one of the following features selected: [')
    }
}

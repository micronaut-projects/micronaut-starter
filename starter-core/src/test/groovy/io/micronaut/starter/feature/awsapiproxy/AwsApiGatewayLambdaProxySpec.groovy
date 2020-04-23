package io.micronaut.starter.feature.awsapiproxy

import io.micronaut.starter.BeanContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll
import io.micronaut.starter.feature.build.gradle.templates.buildGradle
import io.micronaut.starter.feature.build.maven.templates.pom

class AwsApiGatewayLambdaProxySpec extends BeanContextSpec {

    @Shared
    @Subject
    AwsApiGatewayLambdaProxy feature = beanContext.getBean(AwsApiGatewayLambdaProxy)

    @Unroll
    void "aws-api-gateway-lambda-proxy does not support #description"(ApplicationType applicationType,
                                                    String description) {
        expect:
        !feature.supports(applicationType)

        where:
        applicationType << [
                ApplicationType.FUNCTION,
                ApplicationType.CLI,
                ApplicationType.GRPC,
                ApplicationType.MESSAGING
        ]
        description = applicationType.name
    }

    void "aws-lambda supports function application type"() {
        expect:
        feature.supports(ApplicationType.DEFAULT)
    }

    @Unroll
    void 'aws-api-gateway-lambda-proxy is the default feature for function for gradle and language=#language'(Language language) {
        when:
        String template = buildGradle.template(buildProject(), getFeatures(['aws-api-gateway-lambda-proxy'], language, null, BuildTool.GRADLE, ApplicationType.DEFAULT)).render().toString()

        then:
        template.contains('implementation("io.micronaut.aws:micronaut-function-aws-api-proxy")')

        where:
        language << Language.values()
    }

    @Unroll
    void 'test maven micronaut-function-aws-api-proxy feature for language=#language'(Language language) {
        when:
        String template = pom.template(buildProject(), getFeatures(['aws-api-gateway-lambda-proxy'], language, null, BuildTool.GRADLE, ApplicationType.DEFAULT), []).render().toString()

        then:
        template.contains("""
    <dependency>
      <groupId>io.micronaut.aws</groupId>
      <artifactId>micronaut-function-aws-api-proxy</artifactId>
      <scope>compile</scope>
    </dependency>
""")

        where:
        language << Language.values()
    }
}

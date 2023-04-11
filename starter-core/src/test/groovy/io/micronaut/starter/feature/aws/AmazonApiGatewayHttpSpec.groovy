package io.micronaut.starter.feature.aws

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.architecture.Arm
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import spock.lang.IgnoreIf
import spock.lang.Subject
import spock.util.environment.Jvm

@IgnoreIf( value = { Jvm.current.isJava17Compatible() }, reason = "AWS Lambda does not have a Java 17 runtime" )
class AmazonApiGatewayHttpSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    AmazonApiGatewayHttp amazonApiGatewayHttp = beanContext.getBean(AmazonApiGatewayHttp)

    void 'amazon-api-gateway-http feature is in the cloud category'() {
        expect:
        amazonApiGatewayHttp.category == Category.CLOUD
    }

    void 'amazon-api-gateway-http feature is an instance of AwsApiFeature'() {
        expect:
        amazonApiGatewayHttp instanceof AwsApiFeature
        amazonApiGatewayHttp instanceof LambdaTrigger
    }

    void "amazon-api-gateway-http does not support #applicationType application type"(ApplicationType applicationType) {
        expect:
        !amazonApiGatewayHttp.supports(applicationType)

        where:
        applicationType << (ApplicationType.values() - ApplicationType.FUNCTION - ApplicationType.DEFAULT)
    }

    void "amazon-api-gateway-http supports function application type"() {
        expect:
        amazonApiGatewayHttp.supports(ApplicationType.FUNCTION)
        amazonApiGatewayHttp.supports(ApplicationType.DEFAULT)
    }

    void 'amazon-api-gateway-http feature has dependencies, imports and code'() {
        when:
        Map<String, String> output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, BuildTool.GRADLE),
                [Cdk.NAME, AmazonApiGatewayHttp.NAME])

        then:
        output."$Cdk.INFRA_MODULE/build.gradle".contains($/implementation("software.amazon.awscdk:apigatewayv2-alpha/$)
        output."$Cdk.INFRA_MODULE/build.gradle".contains($/implementation("software.amazon.awscdk:apigatewayv2-integrations-alpha/$)

        when:
        String appStack = output."$Cdk.INFRA_MODULE/src/main/java/example/micronaut/AppStack.java"

        then:
        appStack.contains($/import software.amazon.awscdk.services.apigatewayv2.alpha.HttpApi/$)
        appStack.contains($/import software.amazon.awscdk.services.apigatewayv2.integrations.alpha.HttpLambdaIntegration/$)
        appStack.contains($/import static software.amazon.awscdk.services.apigatewayv2.alpha.PayloadFormatVersion.VERSION_1_0/$)

        appStack.contains('''
        HttpLambdaIntegration integration = HttpLambdaIntegration.Builder.create("HttpLambdaIntegration", prodAlias)
                .payloadFormatVersion(VERSION_1_0)
                .build();
        HttpApi api = HttpApi.Builder.create(this, "micronaut-function-api")
                .defaultIntegration(integration)
                .build();
        CfnOutput.Builder.create(this, "MnTestApiUrl")
                .exportName("MnTestApiUrl")
                .value(api.getUrl())
                .build();
''')
    }

    void 'amazon-api-gateway-http and arm do not use SnapStart'() {
        when:
        Map<String, String> output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, BuildTool.GRADLE),
                [Cdk.NAME, AmazonApiGatewayHttp.NAME, Arm.NAME])
        String appStack = output."$Cdk.INFRA_MODULE/src/main/java/example/micronaut/AppStack.java"
        then:
        appStack.contains('''
        HttpLambdaIntegration integration = HttpLambdaIntegration.Builder.create("HttpLambdaIntegration", function)
        ''')
    }

    void 'amazon-api-gateway-http feature without Cdk has dependency in project and doc links'() {
        when:
        def output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, BuildTool.GRADLE),
                [AmazonApiGatewayHttp.NAME])

        then:
        output."build.gradle".contains($/implementation("io.micronaut.aws:micronaut-aws-apigateway/$)
        output."README.md".contains($/https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#amazonApiGateway/$)
        output."README.md".contains($/https://docs.aws.amazon.com/apigateway/$)

    }

    void 'Selecting more than one AmazonApiGateway feature fails with exception'() {
        when:
        generate(ApplicationType.FUNCTION, new Options(Language.JAVA, BuildTool.GRADLE), [Cdk.NAME, AmazonApiGatewayHttp.NAME, AmazonApiGateway.NAME])

        then:
        def e = thrown(IllegalArgumentException)
        e.message.contains("There can only be one of the following features selected:")
    }
}

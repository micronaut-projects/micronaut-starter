package io.micronaut.starter.feature.aws

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.Category
import io.micronaut.starter.feature.architecture.Arm
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.Shared
import spock.lang.Subject

class AmazonApiGatewayHttpSpec extends ApplicationContextSpec implements CommandOutputFixture {

    @Subject
    AmazonApiGatewayHttp amazonApiGatewayHttp = beanContext.getBean(AmazonApiGatewayHttp)

    @Shared
    Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE, AwsLambdaFeatureValidator.firstSupportedJdk())

    void 'amazon-api-gateway-http feature is in the cloud category'() {
        expect:
        amazonApiGatewayHttp.category == Category.SERVERLESS
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

    void 'lambda runtime main class configuration is present for amazon-api-gateway-http and build #buildTool'(BuildTool buildTool) {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, options, [AmazonApiGatewayHttp.NAME])

        then:
        if (buildTool == BuildTool.GRADLE) {
            assert output['build.gradle']
            assert output['build.gradle'].contains('nativeLambda {')
            assert output['build.gradle'].contains('lambdaRuntimeClassName = "io.micronaut.function.aws.runtime.APIGatewayV2HTTPEventMicronautLambdaRuntime"')
        } else if (buildTool == BuildTool.GRADLE) {
            assert output['build.gradle.kts']
            assert output['build.gradle.kts'].contains('nativeLambda {')
            assert output['build.gradle.kts'].contains('lambdaRuntimeClassName.set("io.micronaut.function.aws.runtime.APIGatewayV2HTTPEventMicronautLambdaRuntime")')
        }

        where:
        buildTool << BuildTool.valuesGradle()
    }

    void 'amazon-api-gateway-http feature has dependencies, imports and code'() {
        when:
        Map<String, String> output = generate(ApplicationType.DEFAULT, options,
                [Cdk.NAME, AmazonApiGatewayHttp.NAME])

        then:
        output."$Cdk.INFRA_MODULE/build.gradle".contains($/implementation("software.amazon.awscdk:apigatewayv2-alpha/$)
        output."$Cdk.INFRA_MODULE/build.gradle".contains($/implementation("software.amazon.awscdk:apigatewayv2-integrations-alpha/$)

        when:
        String appStack = output."$Cdk.INFRA_MODULE/src/main/java/example/micronaut/AppStack.java"

        appStack.contains('io.micronaut.function.aws.proxy.payload2.APIGatewayV2HTTPEventFunction')

        then:
        appStack.contains($/import software.amazon.awscdk.services.apigatewayv2.alpha.HttpApi/$)
        appStack.contains($/import software.amazon.awscdk.services.apigatewayv2.integrations.alpha.HttpLambdaIntegration/$)
        !appStack.contains($/import static software.amazon.awscdk.services.apigatewayv2.alpha.PayloadFormatVersion.VERSION_1_0/$)

        appStack.contains('''
        HttpLambdaIntegration integration = HttpLambdaIntegration.Builder.create("HttpLambdaIntegration", prodAlias)
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
        Map<String, String> output = generate(ApplicationType.FUNCTION, options,
                [Cdk.NAME, AmazonApiGatewayHttp.NAME, Arm.NAME])
        String appStack = output."$Cdk.INFRA_MODULE/src/main/java/example/micronaut/AppStack.java"
        then:
        appStack.contains('''
        HttpLambdaIntegration integration = HttpLambdaIntegration.Builder.create("HttpLambdaIntegration", function)
        ''')
    }

    void 'amazon-api-gateway-http feature without Cdk has dependency in project and doc links'() {
        when:
        def output = generate(ApplicationType.FUNCTION, options,
                [AmazonApiGatewayHttp.NAME])

        then:
        output."build.gradle".contains($/implementation("io.micronaut.aws:micronaut-aws-apigateway/$)
        output."README.md".contains($/https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#amazonApiGateway/$)
        output."README.md".contains($/https://docs.aws.amazon.com/apigateway/$)

    }

    void 'Selecting more than one AmazonApiGateway feature fails with exception'() {
        when:
        generate(ApplicationType.FUNCTION, options, [Cdk.NAME, AmazonApiGatewayHttp.NAME, AmazonApiGateway.NAME])

        then:
        def e = thrown(IllegalArgumentException)
        e.message.contains("There can only be one of the following features selected:")
    }
}

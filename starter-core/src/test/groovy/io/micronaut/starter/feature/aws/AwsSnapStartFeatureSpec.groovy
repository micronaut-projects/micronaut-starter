package io.micronaut.starter.feature.aws

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.architecture.Arm
import io.micronaut.starter.feature.function.awslambda.AwsLambda
import io.micronaut.starter.feature.graalvm.GraalVM
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options

class AwsSnapStartFeatureSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'SnapStart does not support ARM #buildTool'() {
        when:
        Map<String, String> output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, buildTool),
                [AwsLambda.FEATURE_NAME_AWS_LAMBDA, Cdk.NAME, AmazonApiGateway.NAME, Arm.NAME])
        String text = output['infra/src/main/java/example/micronaut/AppStack.java']
        then:
        text.contains('.architecture(Architecture.ARM_64)')

        and:
        !hasSnapStart(text)

        where:
        buildTool << BuildTool.values()
    }

    void 'Function AppStack imports included for SnapStart #buildTool'(BuildTool buildTool) {
        when:
        Map<String, String> output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, buildTool),
                [AwsLambda.FEATURE_NAME_AWS_LAMBDA, Cdk.NAME, AmazonApiGateway.NAME])
        String text = output['infra/src/main/java/example/micronaut/AppStack.java']

        then:
        hasSnapStart(text)
        text.contains('import software.amazon.awscdk.services.lambda.Alias;')
        text.contains('import software.amazon.awscdk.services.lambda.CfnFunction;')
        text.contains('import software.amazon.awscdk.services.lambda.Version;')

        where:
        buildTool << graalVmAndCdkSupportedBuilds()
    }

    void 'Function AppStack does not use SnapStart when using graalvm feature #buildTool'(BuildTool buildTool) {
        when:
        Map<String, String> output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, buildTool),
                [AwsLambda.FEATURE_NAME_AWS_LAMBDA, Cdk.NAME, AmazonApiGateway.NAME, GraalVM.FEATURE_NAME_GRAALVM])
        String text = output['infra/src/main/java/example/micronaut/AppStack.java']

        then:
        !hasSnapStart(text)

        where:
        buildTool << graalVmAndCdkSupportedBuilds()
    }

    void 'Function AppStack with SnapStart is included for #buildTool'() {
        when:
        Map<String, String> output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, buildTool),
                [AwsLambda.FEATURE_NAME_AWS_LAMBDA, Cdk.NAME, AmazonApiGateway.NAME])
        String text = output['infra/src/main/java/example/micronaut/AppStack.java']

        then:
        text.contains('.memorySize(2048)')
        hasSnapStart(text)
        usesApiGatewayToSnapStartVersionAlias(text)

        where:
        buildTool << BuildTool.values()
    }

    void 'SnapStart is enabled by default even without a API Gateway #buildTool'() {
        when:
        Map<String, String> output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, buildTool),
                [AwsLambda.FEATURE_NAME_AWS_LAMBDA, Cdk.NAME])
        String text = output['infra/src/main/java/example/micronaut/AppStack.java']

        then:
        hasSnapStart(text)
        !usesApiGatewayToSnapStartVersionAlias(text)

        where:
        buildTool << BuildTool.values()
    }

    private static boolean hasSnapStart(String text) {
        text.contains('cfnFunction.setSnapStart(CfnFunction.SnapStartProperty.builder()')
    }

    private static boolean usesApiGatewayToSnapStartVersionAlias(String text) {
        text.contains('Alias prodAlias = Alias.Builder.create(this, "ProdAlias")')
    }

    private static List<BuildTool> graalVmAndCdkSupportedBuilds() {
        BuildTool.values() - BuildTool.MAVEN
    }
}

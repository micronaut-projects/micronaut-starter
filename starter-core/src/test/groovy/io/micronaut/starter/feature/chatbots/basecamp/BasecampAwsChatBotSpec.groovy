package io.micronaut.starter.feature.chatbots.basecamp

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.aws.AwsLambdaFeatureValidator
import io.micronaut.starter.feature.aws.Cdk
import io.micronaut.starter.feature.chatbots.ChatBotsFeature
import io.micronaut.starter.feature.function.Cloud
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework

class BasecampAwsChatBotSpec extends BaseBasecampChatBotSpec {

    @Override
    List<ApplicationType> getSupportedApplicationTypes() {
        [ApplicationType.FUNCTION]
    }

    @Override
    Class<ChatBotsFeature> getFeature() {
        BasecampAwsChatBot
    }

    @Override
    String getFeatureName() {
        BasecampAwsChatBot.NAME
    }

    void 'chatbots-basecamp-lambda feature is an AWS cloud feature'() {
        expect:
        Cloud.AWS == beanContext.getBean(feature).getCloud()
    }

    void 'test README contains docs for #buildTool'(BuildTool buildTool) {
        when:
        def output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, buildTool), [featureName])
        def readme = output["README.md"]

        then:
        readme.contains("Basecamp ChatBot")
        readme.contains("## Lambda handler class")
        readme.contains("When deployed to AWS Lambda, the lambda handler should be defined as `io.micronaut.chatbots.basecamp.lambda.Handler`.")
        readme.contains("- [Micronaut Validation documentation](https://micronaut-projects.github.io/micronaut-validation/latest/guide/)")
        readme.contains("- [Micronaut AWS Lambda Function documentation](https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambda)")
        readme.contains("- [Micronaut Basecamp ChatBot as AWS Lambda function documentation](https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/)")

        where:
        buildTool << BuildTool.values()
    }

    void 'test README contains docs for #buildTool with CDK'(BuildTool buildTool) {
        when:
        def output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, buildTool), [featureName, 'aws-cdk'])
        def readme = output["README.md"]

        then:
        readme.contains("Basecamp ChatBot")
        readme.contains("## Lambda handler class")
        readme.contains("The Cdk project defined in `infra` is already configured to use `io.micronaut.chatbots.basecamp.lambda.Handler` as the handler for your Lambda function.")
        readme.contains("## How to deploy")
        readme.contains("- [Micronaut Validation documentation](https://micronaut-projects.github.io/micronaut-validation/latest/guide/)")
        readme.contains("- [Micronaut AWS Lambda Function documentation](https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambda)")
        readme.contains("- [Micronaut Basecamp ChatBot as AWS Lambda function documentation](https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/)")
        readme.contains("## Feature aws-cdk documentation")
        readme.contains("- [https://docs.aws.amazon.com/cdk/v2/guide/home.html](https://docs.aws.amazon.com/cdk/v2/guide/home.html)")

        where:
        buildTool << BuildTool.values()
    }

    void 'Handler is is set to io.micronaut.chatbots.basecamp.lambda.Handler in CDK when features chatbots-basecamp-lambda and aws-cdk'() {
        when:
        Options options = new Options(Language.JAVA, TestFramework.JUNIT, BuildTool.GRADLE_KOTLIN, AwsLambdaFeatureValidator.firstSupportedJdk())
        Map<String, String> output = generate(ApplicationType.FUNCTION, options, [BasecampAwsChatBot.NAME, Cdk.NAME])

        then:
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('io.micronaut.chatbots.basecamp.lambda.Handler')

        where:
        buildTool << BuildTool.values()
    }
}

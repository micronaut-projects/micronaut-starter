package io.micronaut.starter.feature.chatbots.telegram

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options

class TelegramAwsChatBotSpec extends BaseTelegramChatBotSpec {

    @Override
    String getFeatureName() {
        TelegramAwsChatBot.NAME
    }

    void 'feature #supportMsg ApplicationType #type'(ApplicationType type, boolean supports) {
        expect:
        beanContext.getBean(TelegramAwsChatBot).supports(type) == supports

        where:
        type << ApplicationType.values()
        supports = type == ApplicationType.FUNCTION
        supportMsg = supports ? 'supports' : 'does not support'
    }

    void 'test README contains docs for #buildTool'(BuildTool buildTool) {
        when:
        def output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, buildTool), [featureName])
        def readme = output["README.md"]

        then:
        readme.contains("Telegram ChatBot")
        readme.contains("## Lambda handler class")
        readme.contains("When deployed to AWS Lambda, the lambda handler should be defined as `io.micronaut.chatbots.telegram.lambda.Handler`.")
        readme.contains("- [Micronaut Validation documentation](https://micronaut-projects.github.io/micronaut-validation/latest/guide/)")
        readme.contains("- [Micronaut AWS Lambda Function documentation](https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambda)")
        readme.contains("- [Micronaut Telegram ChatBot as Lambda documentation](https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/)")

        where:
        buildTool << BuildTool.values()
    }

    void 'test README contains docs for #buildTool with CDK'(BuildTool buildTool) {
        when:
        def output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, buildTool), [featureName, 'aws-cdk'])
        def readme = output["README.md"]

        then:
        readme.contains("Telegram ChatBot")
        readme.contains("## Lambda handler class")
        readme.contains("The Cdk project defined in `infra` is already configured to use `io.micronaut.chatbots.telegram.lambda.Handler` as the handler for your Lambda function.")
        readme.contains("## How to deploy")
        readme.contains("- [Micronaut Validation documentation](https://micronaut-projects.github.io/micronaut-validation/latest/guide/)")
        readme.contains("- [Micronaut AWS Lambda Function documentation](https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambda)")
        readme.contains("- [Micronaut Telegram ChatBot as Lambda documentation](https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/)")
        readme.contains("## Feature aws-cdk documentation")
        readme.contains("- [https://docs.aws.amazon.com/cdk/v2/guide/home.html](https://docs.aws.amazon.com/cdk/v2/guide/home.html)")

        where:
        buildTool << BuildTool.values()
    }
}

package io.micronaut.starter.feature.chatbots.telegram

import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.feature.aws.AwsLambdaFeatureValidator
import io.micronaut.starter.feature.aws.Cdk
import io.micronaut.starter.feature.chatbots.ChatBotsFeature
import io.micronaut.starter.feature.function.Cloud
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework
import spock.lang.Shared

class TelegramAwsChatBotSpec extends BaseTelegramChatBotSpec {

    @Override
    List<ApplicationType> getSupportedApplicationTypes() {
        [ApplicationType.FUNCTION]
    }

    @Override
    Class<ChatBotsFeature> getFeature() {
        TelegramAwsChatBot
    }

    @Override
    String getFeatureName() {
        TelegramAwsChatBot.NAME
    }

    void 'chatbots-telegram-lambda feature is an AWS cloud feature'() {
        expect:
        Cloud.AWS == beanContext.getBean(feature).getCloud()
    }

    void 'test README contains docs for #buildTool'(BuildTool buildTool) {
        when:
        Options options = MicronautOptions.builder().applicationType(ApplicationType.FUNCTION).language(Language.JAVA).buildTool(buildTool).features([featureName]).build()
        Map<String, String> output = generate(options)
        String readme = output["README.md"]

        then:
        readme.contains("Telegram ChatBot")
        readme.contains("## Lambda handler class")
        readme.contains("When deployed to AWS Lambda, the lambda handler should be defined as `io.micronaut.chatbots.telegram.lambda.Handler`.")
        readme.contains("- [https://micronaut-projects.github.io/micronaut-validation/latest/guide/](https://micronaut-projects.github.io/micronaut-validation/latest/guide/)")
        readme.contains("- [https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambda](https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambda)")
        readme.contains("- [https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/](https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/)")

        where:
        buildTool << BuildTool.values()
    }

    void 'test README contains docs for #buildTool with CDK'(BuildTool buildTool) {
        when:
        Options options = MicronautOptions.builder().applicationType(ApplicationType.FUNCTION).language(Language.JAVA).buildTool(buildTool).features([featureName, 'aws-cdk']).build()
        Map<String, String> output = generate(options)
        String readme = output["README.md"]

        then:
        readme.contains("Telegram ChatBot")
        readme.contains("## Lambda handler class")
        readme.contains("The Cdk project defined in `infra` is already configured to use `io.micronaut.chatbots.telegram.lambda.Handler` as the handler for your Lambda function.")
        readme.contains("## How to deploy")
        readme.contains("- [https://micronaut-projects.github.io/micronaut-validation/latest/guide/](https://micronaut-projects.github.io/micronaut-validation/latest/guide/)")
        readme.contains("- [https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambda](https://micronaut-projects.github.io/micronaut-aws/latest/guide/index.html#lambda)")
        readme.contains("- [https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/](https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/)")
        readme.contains("## Feature aws-cdk documentation")
        readme.contains("- [https://docs.aws.amazon.com/cdk/v2/guide/home.html](https://docs.aws.amazon.com/cdk/v2/guide/home.html)")

        where:
        buildTool << BuildTool.values()
    }

    void 'Handler is is set to io.micronaut.chatbots.telegram.lambda.Handler in CDK when features chatbots-telegram-lambda and aws-cdk'() {
        when:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.FUNCTION)
                .language(Language.JAVA)
                .testFramework(TestFramework.JUNIT)
                .buildTool(BuildTool.GRADLE_KOTLIN)
                .javaVersion(AwsLambdaFeatureValidator.firstSupportedJdk())
                .features([TelegramAwsChatBot.NAME, Cdk.NAME])
                .build()
        Map<String, String> output = generate(options)

        then:
        output.'infra/src/main/java/example/micronaut/AppStack.java'.contains('io.micronaut.chatbots.telegram.lambda.Handler')

        where:
        buildTool << BuildTool.values()
    }
}

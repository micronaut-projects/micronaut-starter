package io.micronaut.starter.feature.chatbots.telegram

import io.micronaut.projectgen.core.options.TestFramework
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.feature.aws.AwsLambdaFeatureValidator
import io.micronaut.starter.feature.aws.Cdk
import io.micronaut.starter.feature.chatbots.ChatBotsFeature
import io.micronaut.starter.feature.function.Cloud
import io.micronaut.starter.feature.function.azure.AzureBuildCommandUtils
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import spock.lang.Shared

class TelegramAzureChatBotSpec extends BaseTelegramChatBotSpec {

    @Override
    Class<ChatBotsFeature> getFeature() {
        TelegramAzureChatBot
    }

    @Override
    String getFeatureName() {
        TelegramAzureChatBot.NAME
    }

    @Override
    List<ApplicationType> getSupportedApplicationTypes() {
        [ApplicationType.FUNCTION]
    }

    void 'chatbots-telegram-azure-function feature is an Azure cloud feature'() {
        expect:
        Cloud.AZURE == beanContext.getBean(feature).getCloud()
    }

    void 'test README contains docs for #buildTool and command "#command"'(BuildTool buildTool, String command) {
        when:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.FUNCTION)
                .language(Language.JAVA)
                .buildTool(buildTool)
                .features([featureName])
                .build()
        Map<String, String> output = generate(options)
        String readme = output["README.md"]

        then:
        readme.contains("Telegram ChatBot")
        readme.contains("./$command")
        readme.contains("- [https://micronaut-projects.github.io/micronaut-azure/latest/guide/index.html#simpleAzureFunctions](https://micronaut-projects.github.io/micronaut-azure/latest/guide/index.html#simpleAzureFunctions)")
        readme.contains("- [https://docs.microsoft.com/azure](https://docs.microsoft.com/azure)")
        readme.contains("- [https://micronaut-projects.github.io/micronaut-validation/latest/guide/](https://micronaut-projects.github.io/micronaut-validation/latest/guide/)")
        readme.contains("- [https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/](https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/)")

        where:
        buildTool << BuildTool.values()
        command = buildTool.isGradle() ? AzureBuildCommandUtils.GRADLE_AZURE_DEPLOY_COMMAND : AzureBuildCommandUtils.MAVEN_AZURE_DEPLOY_COMMAND
    }
}

package io.micronaut.starter.feature.chatbots.telegram

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.chatbots.ChatBotsFeature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options

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

    void 'test README contains docs for #buildTool and command "#command"'(BuildTool buildTool, String command) {
        when:
        def output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, buildTool), [featureName])
        def readme = output["README.md"]

        then:
        readme.contains("Telegram ChatBot")
        readme.contains("./$command")
        readme.contains("- [Micronaut Azure Function documentation](https://micronaut-projects.github.io/micronaut-azure/latest/guide/index.html#simpleAzureFunctions)")
        readme.contains("- [https://docs.microsoft.com/azure](https://docs.microsoft.com/azure)")
        readme.contains("- [Micronaut Validation documentation](https://micronaut-projects.github.io/micronaut-validation/latest/guide/)")
        readme.contains("- [Micronaut Telegram ChatBot as Azure Function documentation](https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/)")

        where:
        buildTool << BuildTool.values()
        command = buildTool.isGradle() ? TelegramAzureChatBot.GRADLE_AZURE_DEPLOY_COMMAND : TelegramAzureChatBot.MAVEN_AZURE_DEPLOY_COMMAND
    }
}

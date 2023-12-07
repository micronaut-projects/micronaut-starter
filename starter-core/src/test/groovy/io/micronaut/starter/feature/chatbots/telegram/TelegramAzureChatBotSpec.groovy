package io.micronaut.starter.feature.chatbots.telegram

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.function.Cloud
import io.micronaut.starter.feature.function.azure.AzureBuildCommandUtils
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import spock.lang.Shared

class TelegramAzureChatBotSpec extends BaseTelegramChatBotSpec {

    @Override
    String getFeatureName() {
        TelegramAzureChatBot.NAME
    }

    @Shared
    TelegramAzureChatBot feature = beanContext.getBean(TelegramAzureChatBot)

    void 'chatbots-telegram-azure-function feature is an Azure cloud feature'() {
        expect:
        Cloud.AZURE == feature.getCloud()
    }

    void 'feature #supportMsg ApplicationType #type'(ApplicationType type, boolean supports) {
        expect:
        feature.supports(type) == supports

        where:
        type << ApplicationType.values()
        supports = type == ApplicationType.FUNCTION
        supportMsg = supports ? 'supports' : 'does not support'
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
        readme.contains("- [Micronaut Telegram ChatBot as an Azure Function documentation](https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/)")

        where:
        buildTool << BuildTool.values()
        command = buildTool.isGradle() ? AzureBuildCommandUtils.GRADLE_AZURE_DEPLOY_COMMAND : AzureBuildCommandUtils.MAVEN_AZURE_DEPLOY_COMMAND
    }
}

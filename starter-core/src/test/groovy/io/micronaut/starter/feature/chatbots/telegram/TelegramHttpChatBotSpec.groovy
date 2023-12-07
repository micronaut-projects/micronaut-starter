package io.micronaut.starter.feature.chatbots.telegram

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.chatbots.ChatBotsFeature
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options

class TelegramHttpChatBotSpec extends BaseTelegramChatBotSpec {

    @Override
    List<ApplicationType> getSupportedApplicationTypes() {
        [ApplicationType.DEFAULT]
    }

    @Override
    Class<ChatBotsFeature> getFeature() {
        TelegramHttpChatBot
    }

    @Override
    String getFeatureName() {
        TelegramHttpChatBot.NAME
    }

    void 'test README contains docs for #buildTool'(BuildTool buildTool) {
        when:
        def output = generate(ApplicationType.DEFAULT, new Options(Language.JAVA, buildTool), [featureName])
        def readme = output["README.md"]

        then:
        readme.contains("Telegram ChatBot")
        readme.contains("This project has a dependency on `micronaut-chatbots-telegram-http` which has added a controller to your application with the path `/telegram`.")
        readme.contains("- [Micronaut Validation documentation](https://micronaut-projects.github.io/micronaut-validation/latest/guide/)")
        readme.contains("- [Micronaut Telegram ChatBot as a controller documentation](https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/)")

        where:
        buildTool << BuildTool.values()
    }
}

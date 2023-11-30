package io.micronaut.starter.feature.chatbots.telegram

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.chatbots.BaseChatBotSpec

abstract class BaseTelegramChatBotSpec extends BaseChatBotSpec {

    void 'configuration is generated'() {
        when:
        def output = generate(ApplicationType.FUNCTION, [TelegramAzureChatBot.NAME])
        def cfg = output["src/main/resources/application.properties"]

        then:
        cfg.contains("micronaut.chatbots.folder=botcommands")
        cfg.contains("micronaut.chatbots.telegram.bots.example.token=WEBHOOK_TOKEN")
        cfg.contains("micronaut.chatbots.telegram.bots.example.at-username=@MyMicronautExampleBot")
    }
}

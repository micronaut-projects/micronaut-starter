package io.micronaut.starter.feature.chatbots

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.chatbots.telegram.TelegramAzureChatBot
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options

abstract class BaseChatBotSpec extends ApplicationContextSpec implements CommandOutputFixture {

    void 'example chat commands are generated in #language'(Language language) {
        when:
        def output = generate(ApplicationType.FUNCTION, new Options(language), [TelegramAzureChatBot.NAME])

        then:
        output.containsKey("src/main/$language.name/example/micronaut/AboutCommandHandler.$language.extension".toString())
        output.containsKey("src/main/$language.name/example/micronaut/FinalCommandHandler.$language.extension".toString())
        output.containsKey("src/main/resources/botcommands/about.md")

        where:
        language << Language.values()
    }
}

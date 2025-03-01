package io.micronaut.starter.feature.chatbots.telegram

import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.feature.chatbots.BaseChatBotSpec

import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework

abstract class BaseTelegramChatBotSpec extends BaseChatBotSpec {

    void 'feature #supportMsg ApplicationType #type'(ApplicationType type, boolean supports) {
        expect:
        beanContext.getBean(feature).supports(type) == supports

        where:
        type << ApplicationType.values()
        supports = type in supportedApplicationTypes
        supportMsg = supports ? 'supports' : 'does not support'
    }

    void 'configuration is generated for #applicationType apps'() {
        when:
        def output = generate(applicationType, [featureName])
        def cfg = output["src/main/resources/application.properties"]

        then:
        cfg.contains("micronaut.chatbots.folder=botcommands")
        cfg.contains("micronaut.chatbots.telegram.bots.example.token=WEBHOOK_TOKEN")
        cfg.contains("micronaut.chatbots.telegram.bots.example.at-username=@MyMicronautExampleBot")

        where:
        applicationType << supportedApplicationTypes
    }

    void 'example chat commands are generated in #language for #applicationType apps'(Language language, ApplicationType applicationType) {
        when:
        Options options = MicronautOptions.builder().applicationType(applicationType).language(language).testFramework(TestFramework.JUNIT).features([featureName]).build()
        def output = generate(options)

        then:
        output.containsKey("src/main/$language.name/example/micronaut/AboutCommandHandler.$language.extension".toString())
        output.containsKey("src/main/$language.name/example/micronaut/FinalCommandHandler.$language.extension".toString())
        output.containsKey("src/main/resources/botcommands/about.md")
        output.containsKey(language.getTestSourcePath("/example/micronaut/AboutCommandHandlerTest"))
        output.containsKey("src/test/resources/mockAboutCommand.json")

        where:
        [language, applicationType] << [Language.values(), supportedApplicationTypes].combinations()
    }
}

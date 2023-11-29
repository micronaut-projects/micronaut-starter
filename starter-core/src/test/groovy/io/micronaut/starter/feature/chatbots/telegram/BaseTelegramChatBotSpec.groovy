package io.micronaut.starter.feature.chatbots.telegram

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.chatbots.BaseChatBotSpec
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework

abstract class BaseTelegramChatBotSpec extends BaseChatBotSpec {

    abstract List<ApplicationType> getSupportedApplicationTypes()

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
        def output = generate(applicationType, new Options(language, TestFramework.JUNIT), [featureName])

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

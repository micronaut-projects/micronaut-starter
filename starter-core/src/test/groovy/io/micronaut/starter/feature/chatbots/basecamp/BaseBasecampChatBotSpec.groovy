package io.micronaut.starter.feature.chatbots.basecamp

import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.feature.chatbots.BaseChatBotSpec
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.options.TestFramework

abstract class BaseBasecampChatBotSpec extends BaseChatBotSpec {

    void 'feature #supportMsg ApplicationType #type'(ApplicationType type, boolean supports) {
        expect:
        beanContext.getBean(feature).supports(MicronautOptions.builder().applicationType(type).build()) == supports

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

        where:
        applicationType << supportedApplicationTypes
    }

    void 'example chat commands are generated in #language for #applicationType apps'(Language language, ApplicationType applicationType) {
        when:
        def output = generate(MicronautOptions.builder().applicationType(applicationType).language(language).testFramework(TestFramework.JUNIT).features([featureName]).build())

        then:
        output.containsKey("src/main/$language.name/example/micronaut/BasecampAboutCommandHandler.$language.extension".toString())
        output.containsKey("src/main/$language.name/example/micronaut/BasecampFinalCommandHandler.$language.extension".toString())
        output.containsKey("src/main/resources/botcommands/about.html")
        output.containsKey(language.getTestSourcePath("/example/micronaut/BasecampAboutCommandHandlerTest"))
        output.containsKey("src/test/resources/mockBasecampAboutCommand.json")

        where:
        [language, applicationType] << [Language.values(), supportedApplicationTypes].combinations()
    }
}

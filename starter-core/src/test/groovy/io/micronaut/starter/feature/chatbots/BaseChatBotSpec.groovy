package io.micronaut.starter.feature.chatbots

import io.micronaut.starter.ApplicationContextSpec
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.fixture.CommandOutputFixture
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options

abstract class BaseChatBotSpec extends ApplicationContextSpec implements CommandOutputFixture {

    abstract List<ApplicationType> getSupportedApplicationTypes()

    abstract Class<ChatBotsFeature> getFeature()

    abstract String getFeatureName()

    void 'example chat commands are generated in #language'(Language language) {
        when:
        ApplicationType applicationType = getSupportedApplicationTypes().contains(ApplicationType.FUNCTION) ? ApplicationType.FUNCTION : getSupportedApplicationTypes().stream().findFirst().orElseThrow()
        def output = generate(applicationType, new Options(language), [featureName])

        then:
        output.containsKey("src/main/$language.name/example/micronaut/AboutCommandHandler.$language.extension".toString())
        output.containsKey("src/main/$language.name/example/micronaut/FinalCommandHandler.$language.extension".toString())
        output.containsKey("src/main/resources/botcommands/about.md")

        where:
        language << Language.values()
    }
}

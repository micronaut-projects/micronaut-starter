package io.micronaut.starter.feature.chatbots.basecamp

import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.feature.chatbots.ChatBotsFeature
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options

class BasecampHttpChatBotSpec extends BaseBasecampChatBotSpec {

    @Override
    List<ApplicationType> getSupportedApplicationTypes() {
        [ApplicationType.DEFAULT]
    }

    @Override
    Class<ChatBotsFeature> getFeature() {
        BasecampHttpChatBot
    }

    @Override
    String getFeatureName() {
        BasecampHttpChatBot.NAME
    }

    void 'test README contains docs for #buildTool'(BuildTool buildTool) {
        when:
        Options options = MicronautOptions.builder().applicationType(ApplicationType.DEFAULT).language(Language.JAVA).buildTool(buildTool).features([featureName]).build()
        Map<String, String> output = generate(options)
        String readme = output["README.md"]

        then:
        readme.contains("Basecamp ChatBot")
        readme.contains("This project has a dependency on `micronaut-chatbots-basecamp-http` which has added a controller to your application with the path `/basecamp`.")
        readme.contains("- [Micronaut Validation documentation](https://micronaut-projects.github.io/micronaut-validation/latest/guide/)")
        readme.contains("- [Micronaut Basecamp ChatBot as a controller documentation](https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/)")

        where:
        buildTool << BuildTool.values()
    }
}

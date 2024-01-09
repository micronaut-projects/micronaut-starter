package io.micronaut.starter.feature.chatbots.basecamp

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.chatbots.ChatBotsFeature
import io.micronaut.starter.feature.function.Cloud
import io.micronaut.starter.feature.function.azure.AzureBuildCommandUtils
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options

class BasecampAzureChatBotSpec extends BaseBasecampChatBotSpec {

    @Override
    Class<ChatBotsFeature> getFeature() {
        BasecampAzureChatBot
    }

    @Override
    String getFeatureName() {
        BasecampAzureChatBot.NAME
    }

    @Override
    List<ApplicationType> getSupportedApplicationTypes() {
        [ApplicationType.FUNCTION]
    }

    void 'chatbots-basecamp-azure-function feature is an Azure cloud feature'() {
        expect:
        Cloud.AZURE == beanContext.getBean(feature).getCloud()
    }

    void 'test README contains docs for #buildTool and command "#command"'(BuildTool buildTool, String command) {
        when:
        def output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, buildTool), [featureName])
        def readme = output["README.md"]

        then:
        readme.contains("Basecamp ChatBot")
        readme.contains("./$command")
        readme.contains("- [Micronaut Azure Function documentation](https://micronaut-projects.github.io/micronaut-azure/latest/guide/index.html#simpleAzureFunctions)")
        readme.contains("- [https://docs.microsoft.com/azure](https://docs.microsoft.com/azure)")
        readme.contains("- [Micronaut Validation documentation](https://micronaut-projects.github.io/micronaut-validation/latest/guide/)")
        readme.contains("- [Micronaut Basecamp ChatBot as an Azure Function documentation](https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/)")

        where:
        buildTool << BuildTool.values()
        command = buildTool.isGradle() ? AzureBuildCommandUtils.GRADLE_AZURE_DEPLOY_COMMAND : AzureBuildCommandUtils.MAVEN_AZURE_DEPLOY_COMMAND
    }
}

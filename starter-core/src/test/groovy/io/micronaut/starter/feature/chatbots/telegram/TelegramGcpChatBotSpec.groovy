package io.micronaut.starter.feature.chatbots.telegram

import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.micronaut.MicronautOptions
import io.micronaut.starter.feature.function.Cloud
import io.micronaut.starter.feature.function.gcp.GcpCloudFunctionBuildCommandUtils
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import spock.lang.Shared
import io.micronaut.starter.feature.chatbots.ChatBotsFeature
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options

class TelegramGcpChatBotSpec extends BaseTelegramChatBotSpec {

    @Override
    Class<ChatBotsFeature> getFeature() {
        TelegramGcpChatBot
    }

    @Override
    String getFeatureName() {
        TelegramGcpChatBot.NAME
    }

    List<ApplicationType> getSupportedApplicationTypes() {
        [ApplicationType.FUNCTION]
    }

    void 'chatbots-telegram-gcp-function feature is an GCP cloud feature'() {
        expect:
        Cloud.GCP == beanContext.getBean(feature).getCloud()
    }

    void 'test README contains docs for #buildTool and command "#command"'(BuildTool buildTool, String command) {
        when:
        Options options = MicronautOptions.builder()
                .applicationType(ApplicationType.FUNCTION)
                .language(Language.JAVA)
                .buildTool(buildTool)
                .features([featureName])
                .build()
        Map<String, String> output = generate(options)
        String readme = output["README.md"]

        then:
        readme.contains("Telegram ChatBot")
        readme.contains("./$command")
        readme.contains("- [https://micronaut-projects.github.io/micronaut-gcp/latest/guide/index.html#simpleFunctions](https://micronaut-projects.github.io/micronaut-gcp/latest/guide/index.html#simpleFunctions)")
        readme.contains("- [https://micronaut-projects.github.io/micronaut-validation/latest/guide/](https://micronaut-projects.github.io/micronaut-validation/latest/guide/)")
        readme.contains("- [https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/](https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/)")

        where:
        buildTool << BuildTool.values()
        command = buildTool.isGradle() ? GcpCloudFunctionBuildCommandUtils.GRADLE_PACKAGE_COMMAND : GcpCloudFunctionBuildCommandUtils.MAVEN_PACKAGE_COMMAND
    }
}

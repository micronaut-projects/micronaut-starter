package io.micronaut.starter.feature.chatbots.basecamp

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.chatbots.ChatBotsFeature
import io.micronaut.starter.feature.function.Cloud
import io.micronaut.starter.feature.function.gcp.GcpCloudFunctionBuildCommandUtils
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options

class BasecampGcpChatBotSpec extends BaseBasecampChatBotSpec {

    @Override
    Class<ChatBotsFeature> getFeature() {
        BasecampGcpChatBot
    }

    @Override
    String getFeatureName() {
        BasecampGcpChatBot.NAME
    }

    List<ApplicationType> getSupportedApplicationTypes() {
        [ApplicationType.FUNCTION]
    }

    void 'chatbots-basecamp-gcp-function feature is an GCP cloud feature'() {
        expect:
        Cloud.GCP == beanContext.getBean(feature).getCloud()
    }

    void 'test README contains docs for #buildTool and command "#command"'(BuildTool buildTool, String command) {
        when:
        def output = generate(ApplicationType.FUNCTION, new Options(Language.JAVA, buildTool), [featureName])
        def readme = output["README.md"]

        then:
        readme.contains("Basecamp ChatBot")
        readme.contains("./$command")
        readme.contains("- [Micronaut Google Cloud Function documentation](https://micronaut-projects.github.io/micronaut-gcp/latest/guide/index.html#simpleFunctions)")
        readme.contains("- [Micronaut Validation documentation](https://micronaut-projects.github.io/micronaut-validation/latest/guide/)")
        readme.contains("- [Micronaut Basecamp ChatBot as a Google Cloud Function documentation](https://micronaut-projects.github.io/micronaut-chatbots/latest/guide/)")

        where:
        buildTool << BuildTool.values()
        command = buildTool.isGradle() ? GcpCloudFunctionBuildCommandUtils.GRADLE_PACKAGE_COMMAND : GcpCloudFunctionBuildCommandUtils.MAVEN_PACKAGE_COMMAND
    }
}

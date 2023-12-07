package io.micronaut.starter.core.test.feature.chatbots

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.aws.Cdk
import io.micronaut.starter.feature.chatbots.telegram.TelegramAwsChatBot
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.CommandSpec

class TelegramAwsChatBotFunctionSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "telegramAwsChatBotFunctionSpec"
    }

    void "#testFramework test #feature feature in #language with #buildTool"(BuildTool buildTool, Language language, TestFramework testFramework) {
        when:
        generateProject(language, buildTool, [feature], ApplicationType.FUNCTION, testFramework)

        then:
        String result = executeBuild(buildTool, "test")

        then:
        println result
        result.contains("BUILD SUCCESS")

        where:
        [buildTool, language, testFramework] <<  [BuildTool.values(), Language.values(), TestFramework.values()].combinations()
        feature = TelegramAwsChatBot.NAME
    }

    void "#testFramework test #feature feature with Cdk in #language with #buildTool"(BuildTool buildTool, Language language, TestFramework testFramework) {
        when:
        generateProject(language, buildTool, [feature, Cdk.NAME], ApplicationType.FUNCTION, testFramework)

        then:
        String result = executeBuild(buildTool, "test")

        then:
        println result
        result.contains("BUILD SUCCESS")

        where:
        [buildTool, language, testFramework] <<  [BuildTool.values(), Language.values(), TestFramework.values()].combinations()
        feature = TelegramAwsChatBot.NAME
    }
}

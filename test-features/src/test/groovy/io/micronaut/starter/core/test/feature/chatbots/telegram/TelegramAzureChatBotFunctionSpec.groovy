package io.micronaut.starter.core.test.feature.chatbots.telegram

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.chatbots.telegram.TelegramAzureChatBot
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildTestFrameworkCombinations
import spock.lang.PendingFeature

class TelegramAzureChatBotFunctionSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "telegramAzureChatBotFunctionSpec"
    }

    @PendingFeature(reason = "azure functions do not support 25 yet")
    void "#testFramework test #feature feature in #language with #buildTool"(BuildTool buildTool, Language language, TestFramework testFramework) {
        when:
        generateProject(language, buildTool, [feature], ApplicationType.FUNCTION, testFramework)

        then:
        String result = executeBuild(buildTool, "test")

        then:
        println result
        result.contains("BUILD SUCCESS")

        where:
        [language, buildTool, testFramework] <<  LanguageBuildTestFrameworkCombinations.combinations()
        feature = TelegramAzureChatBot.NAME
    }
}

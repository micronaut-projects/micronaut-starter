package io.micronaut.starter.core.test.feature.chatbots.basecamp

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.chatbots.basecamp.BasecampGcpChatBot
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.CommandSpec

class BasecampGcpChatBotFunctionSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "basecampGcpChatBotFunctionSpec"
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
        feature = BasecampGcpChatBot.NAME
    }
}

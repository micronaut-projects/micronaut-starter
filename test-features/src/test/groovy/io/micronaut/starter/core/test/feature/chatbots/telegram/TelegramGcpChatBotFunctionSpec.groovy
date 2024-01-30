package io.micronaut.starter.core.test.feature.chatbots.telegram

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.feature.chatbots.telegram.TelegramGcpChatBot
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.test.CommandSpec

class TelegramGcpChatBotFunctionSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "telegramGcpChatBotFunctionSpec"
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
                .stream()
                .filter(l -> !(l[0] == BuildTool.MAVEN && l[1] == Language.KOTLIN) ) // Caused by: java.lang.NoSuchMethodError: Micronaut method io.micronaut.context.DefaultBeanContext.getProxyTargetBean(BeanResolutionContext,BeanDefinition,Argument,Qualifier) not found. Most likely reason for this issue is that you are running a newer version of Micronaut with code compiled against an older version. Please recompile the offending classe"
                .toList()
        feature = TelegramGcpChatBot.NAME
    }
}

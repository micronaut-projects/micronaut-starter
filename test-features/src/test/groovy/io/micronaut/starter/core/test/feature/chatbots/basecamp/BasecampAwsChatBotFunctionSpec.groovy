package io.micronaut.starter.core.test.feature.chatbots.basecamp

import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.feature.aws.Cdk
import io.micronaut.starter.feature.chatbots.basecamp.BasecampAwsChatBot
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.TestFramework
import io.micronaut.starter.test.CommandSpec

class BasecampAwsChatBotFunctionSpec extends CommandSpec {

    @Override
    String getTempDirectoryPrefix() {
        return "basecampAwsChatBotFunctionSpec"
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
        feature = BasecampAwsChatBot.NAME
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
        feature = BasecampAwsChatBot.NAME
    }
}

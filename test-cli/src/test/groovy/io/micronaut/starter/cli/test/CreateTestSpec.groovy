package io.micronaut.starter.cli.test

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.command.project.test.CreateTestCommand
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildTestFrameworkCombinations
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class CreateTestSpec extends CommandSpec {

    @Unroll
    void "test create-test for #language and #testFramework and #buildTool"(Language language,
                                                                            BuildTool buildTool,
                                                                            TestFramework testFramework) {
        generateProject(language, buildTool, [] as List<String>, ApplicationType.DEFAULT, testFramework)
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateTestCommand command = new CreateTestCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.testName = "Greeting"

        when:
        Integer exitCode = command.call()
        File output = new File(dir, testFramework.getSourcePath("/example/micronaut/Greeting", language))

        then:
        exitCode == 0
        output.exists()
        1 * consoleOutput.out({ it.contains("Rendered test") })

        when:
        String result = null
        if (buildTool.isGradle()) {
            result = executeGradle("test")?.output
        } else if (buildTool == BuildTool.MAVEN) {
            result = executeMaven("compile test")
        }

        then:
        result?.contains("BUILD SUCCESS")

        where:
        [language, buildTool, testFramework] << LanguageBuildTestFrameworkCombinations.combinations()
    }

    @Override
    String getTempDirectoryPrefix() {
        "test-createtest"
    }
}

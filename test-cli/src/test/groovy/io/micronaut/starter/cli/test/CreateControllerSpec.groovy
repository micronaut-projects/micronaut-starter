package io.micronaut.starter.cli.test

import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.feature.server.controller.CreateControllerCommand
import io.micronaut.projectgen.core.io.ConsoleOutput
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.TestFramework
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildTestFrameworkCombinations

class CreateControllerSpec extends CommandSpec {

    void "test creating a controller and running the test for #language and #testFramework and #buildTool"(Language language,
                                                                                                           BuildTool buildTool,
                                                                                                           TestFramework testFramework) {

        generateProject(language, buildTool, [] as List<String>, ApplicationType.DEFAULT, testFramework)
            CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateControllerCommand command = new CreateControllerCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput, [])
        command.controllerName = "Greeting"

        when:
        Integer exitCode = command.call()

        then:
        exitCode == 0
        new File(dir, language.getSourcePath("/example/micronaut/GreetingController")).exists()
        new File(dir, testFramework.getSourcePath("/example/micronaut/GreetingController", language)).exists()
        1 * consoleOutput.out({ it.contains("Rendered controller") })
        1 * consoleOutput.out({ it.contains("Rendered test") })

        when:
        String output = executeBuild(buildTool, "test")

        then:
        output?.contains("BUILD SUCCESS")

        where:
        [language, buildTool, testFramework] << LanguageBuildTestFrameworkCombinations.combinations()
    }

    @Override
    String getTempDirectoryPrefix() {
        "test-createcontroller-createcontrollergroovygradlejunitspec"
    }
}

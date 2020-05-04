package io.micronaut.starter.cli.feature.server.controller

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.generator.CommandSpec
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import spock.lang.Unroll

class CreateControllerJavaGradleJunitSpec extends CommandSpec {

    @Unroll
    void "test creating a controller and running the test for #language and #testFramework and #buildTool"(Language language, BuildTool buildTool, TestFramework testFramework) {

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
        if (buildTool == BuildTool.GRADLE) {
            executeGradleCommand("test")
        } else if (buildTool == BuildTool.MAVEN) {
            executeMavenCommand("test")
        }

        then:
        testOutputContains("BUILD SUCCESS")

        where:
        language      | buildTool        | testFramework
        Language.JAVA | BuildTool.GRADLE | TestFramework.JUNIT
    }

    @Override
    String getTempDirectoryPrefix() {
        "starter-core-test-createcontroller-createcontrollerjavagradlejunitspec"
    }
}

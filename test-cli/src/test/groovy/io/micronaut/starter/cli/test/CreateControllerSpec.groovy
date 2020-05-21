package io.micronaut.starter.cli.test

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.feature.server.controller.CreateControllerCommand
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildTestFrameworkCombinations
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.TestFramework
import org.apache.maven.cli.MavenCli
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Shared
import spock.lang.Unroll

import java.nio.charset.StandardCharsets

class CreateControllerSpec extends CommandSpec {
    @Shared GradleRunner gradleRunner = GradleRunner.create()
    @Shared MavenCli cli = new MavenCli()

    @Unroll
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
        String output = null
        if (buildTool == BuildTool.GRADLE) {
            output = executeGradle("test").getOutput()
        } else if (buildTool == BuildTool.MAVEN) {
            output = executeMaven("test")
        }

        then:
        output?.contains("BUILD SUCCESS")

        where:
        [language, buildTool, testFramework] << LanguageBuildTestFrameworkCombinations.combinations()
    }

    @Override
    String getTempDirectoryPrefix() {
        "test-createcontroller-createcontrollergroovygradlejunitspec"
    }

    BuildResult executeGradle(String command) {
        BuildResult result =
                gradleRunner.withProjectDir(dir)
                            .withArguments(command)
                            .build()
        return result
    }

    String executeMaven(String command) {
        System.setProperty("maven.multiModuleProjectDirectory", dir.absolutePath)
        def bytesOut = new ByteArrayOutputStream()
        PrintStream output = new PrintStream(bytesOut)
        cli.doMain(command.split(' '), dir.absolutePath, output, output)
        return new String(bytesOut.toByteArray(), StandardCharsets.UTF_8)
    }
}

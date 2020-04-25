package io.micronaut.starter.cli.command.project

import io.micronaut.context.BeanContext
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.CommandFixture
import io.micronaut.starter.cli.CommandSpec
import io.micronaut.starter.cli.command.project.test.CreateTestCommand
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Unroll

class CreateTestSpec extends CommandSpec implements CommandFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void "test create-test for #language.getName() and #testFramework.getName() and #build.getName()"(Language language, TestFramework testFramework, BuildTool build) {
        generateDefaultProject(new Options(language, testFramework, build))
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
        if (build == BuildTool.GRADLE) {
            executeGradleCommand("test")
        } else if (build == BuildTool.MAVEN) {
            executeMavenCommand("compile test")
        }

        then:
        testOutputContains("BUILD SUCCESS")

        where:
        [language, testFramework, build] << [Language.values(), TestFramework.values(), BuildTool.values()].combinations()
    }
}

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
    void "test create-test for #language.getName() and #testFramework.getName()"() {
        generateDefaultProject(new Options(language, testFramework, BuildTool.GRADLE))
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateTestCommand command = new CreateTestCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.testName = "Greeting"

        when:
        Integer exitCode = command.call()
        File output = new File(dir, file)

        then:
        exitCode == 0
        output.exists()
        1 * consoleOutput.out({ it.contains("Rendered test") })

        when:
        executeGradleCommand("test")

        then:
        testOutputContains("BUILD SUCCESSFUL")

        where:
        language        | testFramework            | file
        Language.JAVA   | TestFramework.JUNIT      | "src/test/java/example/micronaut/GreetingTest.java"
        Language.GROOVY | TestFramework.JUNIT      | "src/test/groovy/example/micronaut/GreetingTest.groovy"
        Language.KOTLIN | TestFramework.JUNIT      | "src/test/kotlin/example/micronaut/GreetingTest.kt"
        Language.GROOVY | TestFramework.SPOCK      | "src/test/groovy/example/micronaut/GreetingSpec.groovy"
        Language.KOTLIN | TestFramework.KOTLINTEST | "src/test/kotlin/example/micronaut/GreetingTest.kt"
    }
}

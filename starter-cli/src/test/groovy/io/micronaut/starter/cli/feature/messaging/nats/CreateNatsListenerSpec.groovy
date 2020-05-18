package io.micronaut.starter.cli.feature.messaging.nats

import io.micronaut.context.BeanContext
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.CommandFixture
import io.micronaut.starter.cli.CommandSpec
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Unroll

class CreateNatsListenerSpec extends CommandSpec implements CommandFixture {

    @Shared @AutoCleanup BeanContext beanContext = BeanContext.run()

    @Unroll
    void "test the project compiles after creating a listener #language - #buildTool"(Language language, BuildTool buildTool) {
        given:
        generateProject(new Options(language, buildTool), ['nats'])
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateNatsListener command = new CreateNatsListener(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.listenerName = "Greeting"

        expect:
        command.applies()

        when:
        command.call()

        then:
        1 * consoleOutput.out({ it.contains("Rendered Nats listener") })

        when:
        if (buildTool == BuildTool.GRADLE) {
            executeGradleCommand("classes")
        } else if (buildTool == BuildTool.MAVEN) {
            executeMavenCommand("compile")
        }

        then:
        testOutputContains("BUILD SUCCESS")

        where:
        [language, buildTool] << [Language.values(), BuildTool.values()].combinations()
    }

}

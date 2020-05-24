package io.micronaut.starter.cli.test

import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.feature.messaging.nats.CreateNatsProducer
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import io.micronaut.starter.test.LanguageBuildCombinations
import spock.lang.Unroll

class CreateNatsProducerSpec extends CommandSpec {

    @Unroll
    void "test the project compiles after creating a listener #language - #buildTool"(Language language, BuildTool buildTool) {
        given:
        generateProject(language, buildTool, ['nats'] as List<String>)
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateNatsProducer command = new CreateNatsProducer(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.producerName = "Greeting"

        expect:
        command.applies()

        when:
        command.call()

        then:
        1 * consoleOutput.out({ it.contains("Rendered Nats producer") })

        when:
        if (buildTool == BuildTool.GRADLE) {
            executeGradleCommand("classes")
        } else if (buildTool == BuildTool.MAVEN) {
            executeMavenCommand("compile")
        }

        then:
        testOutputContains("BUILD SUCCESS")

        where:
        [language, buildTool] << LanguageBuildCombinations.combinations()
    }

    @Override
    String getTempDirectoryPrefix() {
        "test-createNatsProducer"
    }

}

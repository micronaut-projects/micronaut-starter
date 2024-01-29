package io.micronaut.starter.cli.test

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
        String output = null
        if (buildTool.isGradle()) {
            output = executeGradle("classes")?.output
        } else if (buildTool == BuildTool.MAVEN) {
            output = executeMaven("compile")
        }

        then:
        output?.contains("BUILD SUCCESS")

        where:
        [language, buildTool] << LanguageBuildCombinations.combinations()
                .stream()
                .filter(l -> !(l[0] == Language.KOTLIN && l[1] == BuildTool.MAVEN) ) // Caused by: java.lang.NoSuchMethodError: Micronaut method io.micronaut.context.DefaultBeanContext.getProxyTargetBean(BeanResolutionContext,BeanDefinition,Argument,Qualifier) not found. Most likely reason for this issue is that you are running a newer version of Micronaut with code compiled against an older version. Please recompile the offending classe"
    }

    @Override
    String getTempDirectoryPrefix() {
        "test-createNatsProducer"
    }

}

package io.micronaut.starter.cli.feature.messaging.jms

import io.micronaut.context.ApplicationContext
import io.micronaut.context.BeanContext
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.CommandFixture
import io.micronaut.starter.cli.CommandSpec
import io.micronaut.starter.feature.messaging.jms.ActiveMqArtemis
import io.micronaut.starter.feature.messaging.jms.ActiveMqClassic
import io.micronaut.starter.feature.messaging.jms.SQS
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Unroll

class CreateJmsConsumerSpec extends CommandSpec implements CommandFixture {
    @Shared
    @AutoCleanup
    ApplicationContext beanContext = ApplicationContext.run()

    @Unroll
    void "test creating jms-activemq-artemis consumer - #language.getName()"(Language language) {
        generateProject(language, BuildTool.GRADLE, [ActiveMqArtemis.NAME])
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateJmsConsumer command = new CreateJmsConsumer(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.consumerName = "User"

        when:
        Integer exitCode = command.call()
        File output = new File(dir, language.getSourcePath("/example/micronaut/User"))

        then:
        exitCode == 0
        output.exists()
        output.text.contains("import io.micronaut.jms.annotations.JMSListener")
        output.text.contains("import static io.micronaut.jms.activemq.artemis.configuration.ActiveMqArtemisConfiguration.CONNECTION_FACTORY_BEAN_NAME")
        output.text.contains("@JMSListener(CONNECTION_FACTORY_BEAN_NAME)")
        1 * consoleOutput.out({ it.contains("Rendered JMS consumer") })

        where:
        language << Language.values()
    }

    @Unroll
    void "test creating jms-activemq-classic consumer - #language.getName()"(Language language) {
        generateProject(language, BuildTool.GRADLE, [ActiveMqClassic.NAME])
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateJmsConsumer command = new CreateJmsConsumer(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.consumerName = "User"

        when:
        Integer exitCode = command.call()
        File output = new File(dir, language.getSourcePath("/example/micronaut/User"))

        then:
        exitCode == 0
        output.exists()
        output.text.contains("import io.micronaut.jms.annotations.JMSListener")
        output.text.contains("import static io.micronaut.jms.activemq.classic.configuration.ActiveMqClassicConfiguration.CONNECTION_FACTORY_BEAN_NAME")
        output.text.contains("@JMSListener(CONNECTION_FACTORY_BEAN_NAME)")
        1 * consoleOutput.out({ it.contains("Rendered JMS consumer") })

        where:
        language << Language.values()
    }

    @Unroll
    void "test creating jms-sqs consumer - #language.getName()"(Language language) {
        generateProject(language, BuildTool.GRADLE, [SQS.NAME])
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateJmsConsumer command = new CreateJmsConsumer(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.consumerName = "User"

        when:
        Integer exitCode = command.call()
        File output = new File(dir, language.getSourcePath("/example/micronaut/User"))

        then:
        exitCode == 0
        output.exists()
        output.text.contains("import io.micronaut.jms.annotations.JMSListener")
        output.text.contains("import static io.micronaut.jms.sqs.configuration.SqsConfiguration.CONNECTION_FACTORY_BEAN_NAME")
        output.text.contains("@JMSListener(CONNECTION_FACTORY_BEAN_NAME)")
        1 * consoleOutput.out({ it.contains("Rendered JMS consumer") })

        where:
        language << Language.values()
    }
}

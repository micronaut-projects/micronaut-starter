package io.micronaut.starter.cli.feature.messaging.mqtt

import io.micronaut.context.BeanContext
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.CommandFixture
import io.micronaut.starter.cli.CommandSpec
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Unroll

class CreateMqttSubscriberSpec extends CommandSpec implements CommandFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void "test creating an mqtt #version subscriber - #language.getName()"(Language language, String version) {
        generateProject(language, BuildTool.GRADLE, [version == "v5" ? "mqtt" : "mqtt${version}".toString()])
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateMqttSubscriber command = new CreateMqttSubscriber(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.listenerName = "User"

        when:
        Integer exitCode = command.call()
        File output = new File(dir, language.getSourcePath("/example/micronaut/User"))

        then:
        exitCode == 0
        output.exists()
        output.text.contains("import io.micronaut.mqtt.annotation.MqttSubscriber".toString())
        1 * consoleOutput.out({ it.contains("Rendered MQTT subscriber") })

        where:
        [language, version] << [Language.values(), ["v3", "v5"]].combinations()
    }

}

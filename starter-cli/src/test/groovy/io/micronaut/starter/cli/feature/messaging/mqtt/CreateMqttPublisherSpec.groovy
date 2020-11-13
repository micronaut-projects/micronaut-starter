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

class CreateMqttPublisherSpec extends CommandSpec implements CommandFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void "test creating an mqtt #version publisher - #language.getName()"(Language language) {
        generateProject(language, BuildTool.GRADLE, [version == "v5" ? "mqtt" : "mqtt${version}".toString()])
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateMqttPublisher command = new CreateMqttPublisher(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.publisherName = "User"

        when:
        Integer exitCode = command.call()
        File output = new File(dir, language.getSourcePath("/example/micronaut/User"))

        then:
        exitCode == 0
        output.exists()
        output.text.contains("import io.micronaut.mqtt.${version}.annotation.MqttPublisher".toString())
        1 * consoleOutput.out({ it.contains("Rendered MQTT publisher") })

        where:
        [language, version] << [Language.values(), ["v3", "v5"]].combinations()
    }

}

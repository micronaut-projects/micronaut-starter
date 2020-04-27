package io.micronaut.starter.cli.feature.server.websocket

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

class CreateWebsocketServerSpec extends CommandSpec implements CommandFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void "test creating a websocket server - #language.getName()"(Language language) {
        generateProject(language, BuildTool.GRADLE)
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateWebsocketServerCommand command = new CreateWebsocketServerCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput, [])
        command.serverName = "User"

        when:
        Integer exitCode = command.call()
        File output = new File(dir, language.getSourcePath("/example/micronaut/UserServer"))

        then:
        exitCode == 0
        output.exists()
        1 * consoleOutput.out({ it.contains("Rendered websocket server") })

        where:
        language << Language.values()
    }
}

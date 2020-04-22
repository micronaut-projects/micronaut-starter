package io.micronaut.starter.cli.feature.server.websocket


import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.CommandSpec
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CreateWebsocketServerSpec extends CommandSpec {

    @Unroll
    void "test creating a websocket server - #language.getName()"() {
        generateDefaultProject(language, BuildTool.GRADLE)
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateWebsocketServerCommand command = new CreateWebsocketServerCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput, [])
        command.serverName = "User"

        when:
        Integer exitCode = command.call()
        File output = new File(dir, file)

        then:
        exitCode == 0
        output.exists()
        1 * consoleOutput.out({ it.contains("Rendered websocket server") })

        where:
        language        | file
        Language.JAVA   | "src/main/java/example/micronaut/UserServer.java"
        Language.KOTLIN | "src/main/kotlin/example/micronaut/UserServer.kt"
        Language.GROOVY | "src/main/groovy/example/micronaut/UserServer.groovy"
    }
}

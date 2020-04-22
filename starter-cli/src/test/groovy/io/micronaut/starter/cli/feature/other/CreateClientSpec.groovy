package io.micronaut.starter.cli.feature.other

import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.CommandSpec
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.Unroll

class CreateClientSpec extends CommandSpec {

    @Unroll
    void "test creating a client - #language.getName()"() {
        generateDefaultProject(language, BuildTool.GRADLE)
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateClientCommand command = new CreateClientCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.clientName = "User"

        when:
        Integer exitCode = command.call()
        File output = new File(dir, file)

        then:
        exitCode == 0
        output.exists()
        1 * consoleOutput.out({ it.contains("Rendered client") })

        where:
        language        | file
        Language.JAVA   | "src/main/java/example/micronaut/UserClient.java"
        Language.KOTLIN | "src/main/kotlin/example/micronaut/UserClient.kt"
        Language.GROOVY | "src/main/groovy/example/micronaut/UserClient.groovy"
    }

}

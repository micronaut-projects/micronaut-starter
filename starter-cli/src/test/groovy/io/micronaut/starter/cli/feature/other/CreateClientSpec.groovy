package io.micronaut.starter.cli.feature.other

import io.micronaut.context.BeanContext
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.CommandFixture
import io.micronaut.starter.cli.CommandSpec
import io.micronaut.starter.cli.feature.database.CreateRepositoryCommand
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Unroll

class CreateClientSpec extends CommandSpec implements CommandFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void "test creating a client - #language.getName()"(Language language) {
        generateDefaultProject(language, BuildTool.GRADLE)
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateClientCommand command = new CreateClientCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.clientName = "User"

        when:
        Integer exitCode = command.call()
        File output = new File(dir, language.getSourcePath("/example/micronaut/UserClient"))

        then:
        exitCode == 0
        output.exists()
        1 * consoleOutput.out({ it.contains("Rendered client") })

        where:
        language << Language.values()
    }

}

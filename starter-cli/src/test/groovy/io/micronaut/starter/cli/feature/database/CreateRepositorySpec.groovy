package io.micronaut.starter.cli.feature.database

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

class CreateRepositorySpec extends CommandSpec implements CommandFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void "test creating a repository - #language.getName()"(Language language) {
        generateProject(language, BuildTool.GRADLE, ['data-jpa'])
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateRepositoryCommand command = new CreateRepositoryCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.repositoryName = "User"

        when:
        Integer exitCode = command.call()
        File output = new File(dir, language.getSourcePath("/example/micronaut/UserRepository"))

        then:
        exitCode == 0
        output.exists()
        1 * consoleOutput.out({ it.contains("Rendered repository") })

        where:
        language << Language.values()
    }

    void "test creating a repository with an invalid id type"() {
        generateProject(Language.JAVA, BuildTool.GRADLE, ['data-jpa'])
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateRepositoryCommand command = new CreateRepositoryCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.repositoryName = "User"
        command.idType = "Foo"

        when:
        command.call()

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Code generation not supported for the specified id type: Foo. Please specify the fully qualified class name."
    }

}
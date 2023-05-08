package io.micronaut.starter.cli.feature.database

import io.micronaut.context.ApplicationContext
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.CommandFixture
import io.micronaut.starter.cli.CommandSpec
import io.micronaut.starter.feature.database.DatabaseDriverFeature
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.JdkVersion
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.MicronautJdkVersionConfiguration
import io.micronaut.starter.options.Options
import io.micronaut.starter.util.VersionInfo
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Unroll

class CreateRepositorySpec extends CommandSpec implements CommandFixture {

    @Shared
    @AutoCleanup
    ApplicationContext beanContext = ApplicationContext.run()

    @Unroll
    void "test creating a repository - #language.getName()"(Language language) {
        generateProject(language, BuildTool.GRADLE, ['data-jpa'])
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateRepositoryCommand command = new CreateRepositoryCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput, beanContext.getBeansOfType(DatabaseDriverFeature).toList())
        command.repositoryName = "User"

        when:
        Integer exitCode = command.call()
        File output = new File(dir, language.getSourcePath("/example/micronaut/UserRepository"))

        then:
        exitCode == 0
        output.exists()
        output.text.contains('@Repository\n')
        1 * consoleOutput.out({ it.contains("Rendered repository") })

        where:
        language << Language.values()
    }

    @Unroll
    void "test creating a jdbc repository - #language.getName()"(Language language) {
        generateProject(language, BuildTool.GRADLE, ['data-jdbc'])
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateRepositoryCommand command = new CreateRepositoryCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput, beanContext.getBeansOfType(DatabaseDriverFeature).toList())
        command.repositoryName = "User"

        when:
        Integer exitCode = command.call()
        File output = new File(dir, language.getSourcePath("/example/micronaut/UserRepository"))

        then:
        exitCode == 0
        output.exists()
        output.text.contains('@JdbcRepository(dialect = Dialect.H2)\n')
        1 * consoleOutput.out({ it.contains("Rendered repository") })

        where:
        language << Language.values()
    }

    @Unroll
    void "test creating a repository for #language and #driverFeature.name"(Language language, DatabaseDriverFeature driverFeature) {
        boolean forceJava11 = driverFeature.name == "oracle-cloud-atp" && VersionInfo.getJavaVersion() == JdkVersion.JDK_8
        if (forceJava11) {
            generateProject(new Options(language, null, BuildTool.GRADLE, MicronautJdkVersionConfiguration.DEFAULT_OPTION),
                    ['data-jdbc', driverFeature.getName()])
        } else {
            generateProject(language, BuildTool.GRADLE, ['data-jdbc', driverFeature.getName()])
        }

        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateRepositoryCommand command = new CreateRepositoryCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput, beanContext.getBeansOfType(DatabaseDriverFeature).toList())
        command.repositoryName = "User"

        when:
        Integer exitCode = command.call()
        File output = new File(dir, language.getSourcePath("/example/micronaut/UserRepository"))

        then:
        exitCode == 0
        output.exists()
        output.text.contains('@JdbcRepository(dialect = Dialect.' + driverFeature.getDataDialect() + ')\n')
        1 * consoleOutput.out({ it.contains("Rendered repository") })

        where:
        [language, driverFeature] << [Language.values(), beanContext.getBeansOfType(DatabaseDriverFeature).toList()].combinations()
    }

    void "test creating a repository with an invalid id type"() {
        generateProject(Language.JAVA, BuildTool.GRADLE, ['data-jpa'])
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateRepositoryCommand command = new CreateRepositoryCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput, beanContext.getBeansOfType(DatabaseDriverFeature).toList())
        command.repositoryName = "User"
        command.idType = "Foo"

        when:
        command.call()

        then:
        def ex = thrown(IllegalArgumentException)
        ex.message == "Code generation not supported for the specified id type: Foo. Please specify the fully qualified class name."
    }

}

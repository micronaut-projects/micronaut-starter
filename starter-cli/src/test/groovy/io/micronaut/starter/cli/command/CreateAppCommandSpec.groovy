package io.micronaut.starter.cli.command

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Parameter
import io.micronaut.context.env.Environment
import io.micronaut.core.util.functional.ThrowingSupplier
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.OperatingSystem
import io.micronaut.starter.application.Project
import io.micronaut.starter.application.generator.ProjectGenerator
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.CommandFixture
import io.micronaut.starter.cli.CommandSpec
import io.micronaut.starter.cli.feature.other.CreateClientCommand
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.io.FileSystemOutputHandler
import io.micronaut.starter.io.OutputHandler
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.util.NameUtils
import spock.lang.AutoCleanup
import spock.lang.Issue
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import javax.inject.Inject

class CreateAppCommandSpec extends CommandSpec implements CommandFixture {

    @Shared
    @AutoCleanup
    ApplicationContext ctx = ApplicationContext.run(Environment.CLI)

    @Shared
    @AutoCleanup
    ApplicationContext beanContext = ApplicationContext.run()

    @Issue("https://github.com/micronaut-projects/micronaut-starter/issues/352")
    void "test micronaut is not a valid application name"() {
        given:
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        System.setErr(new PrintStream(baos))

        when:
        PicocliRunner.run(CreateAppCommand, ctx, "micronaut")

        then:
        noExceptionThrown()
        baos.toString().contains("\"micronaut\" is not a valid app name")
    }

    @Unroll
    void "test creating project with defaults for language=#language"(Language language) {

        when:
        createProject(language)
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)

        CreateSpecAppCommand command = new CreateSpecAppCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        Integer exitCode = command.call()
        File testOutput = new File(dir, language.defaults.test.getSourcePath("/example/micronaut/Foo", language))
        File settings = new File(dir, language.defaults.build.buildFileName)

        then:
        exitCode == 0
        testOutput.exists()
        settings.exists()

        where:
        language << Language.values()
    }

    void createProject(Language lang) {
        beanContext.getBean(ProjectGenerator).generate(ApplicationType.DEFAULT,
                NameUtils.parse("example.micronaut.foo"),
                new Options(lang),
                OperatingSystem.LINUX,
                [],
                new FileSystemOutputHandler(dir, ConsoleOutput.NOOP),
                ConsoleOutput.NOOP
        )
    }

    class CreateSpecAppCommand extends CodeGenCommand {
        @Inject
        CreateSpecAppCommand(@Parameter CodeGenConfig config) {
            super(config);
        }

        CreateSpecAppCommand(CodeGenConfig config, ThrowingSupplier<OutputHandler, IOException> outputHandlerSupplier, ConsoleOutput consoleOutput) {
            super(config, outputHandlerSupplier, consoleOutput);
        }

        @Override
        boolean applies() {
            true
        }

        @Override
        Integer call() throws Exception {
            return 0
        }
    }
}

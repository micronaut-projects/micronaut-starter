package io.micronaut.starter.cli.command.project

import io.micronaut.context.BeanContext
import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.CommandFixture
import io.micronaut.starter.cli.CommandSpec
import io.micronaut.starter.cli.command.project.bean.CreateBeanCommand
import io.micronaut.starter.cli.command.project.job.CreateJobCommand
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.Language
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Unroll

class CreateJobSpec extends CommandSpec implements CommandFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    @Unroll
    void "test creating a job - #language.getName()"(Language language) {
        generateDefaultProject(language)
        CodeGenConfig codeGenConfig = CodeGenConfig.load(beanContext, dir, ConsoleOutput.NOOP)
        ConsoleOutput consoleOutput = Mock(ConsoleOutput)
        CreateJobCommand command = new CreateJobCommand(codeGenConfig, getOutputHandler(consoleOutput), consoleOutput)
        command.jobName = "Ticker"

        when:
        Integer exitCode = command.call()
        File output = new File(dir, language.getSourcePath("/example/micronaut/Ticker"))

        then:
        exitCode == 0
        output.exists()
        1 * consoleOutput.out({ it.contains("Rendered job") })

        where:
        language << Language.values()
    }

}

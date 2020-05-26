package io.micronaut.starter.cli.test

import io.micronaut.starter.cli.CodeGenConfig
import io.micronaut.starter.cli.command.project.job.CreateJobCommand
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.Language
import io.micronaut.starter.test.CommandSpec
import spock.lang.Unroll

class CreateJobSpec extends CommandSpec {

    @Unroll
    void "test creating a job - #language.getName()"(Language language) {
        generateProject(language)
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

    @Override
    String getTempDirectoryPrefix() {
        "test-createJob"
    }
}

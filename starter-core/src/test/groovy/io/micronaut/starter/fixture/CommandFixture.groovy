package io.micronaut.starter.fixture

import io.micronaut.context.BeanContext
import io.micronaut.starter.OutputHandler
import io.micronaut.starter.command.CreateAppCommand
import io.micronaut.starter.command.CreateCliCommand
import io.micronaut.starter.io.FileSystemOutputHandler
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language

trait CommandFixture {

    abstract BeanContext getBeanContext()

    abstract File getDir()

    CreateCliCommand runCreateCliCommand(Language lang, BuildTool buildTool = BuildTool.gradle, List <String> features = []) {
        CreateCliCommand command = beanContext.getBean(CreateCliCommand)
        command.name = 'example.micronaut.foo'
        command.lang = lang
        command.build = buildTool
        command.features = features

        OutputHandler outputHandler = new FileSystemOutputHandler(dir, command)
        command.generate(outputHandler)

        command
    }

    CreateAppCommand runCreateAppCommand(Language lang, BuildTool buildTool = BuildTool.gradle, List <String> features = []) {
        CreateAppCommand command = beanContext.getBean(CreateAppCommand)
        command.name = 'example.micronaut.foo'
        command.lang = lang
        command.build = buildTool
        command.features = features

        OutputHandler outputHandler = new FileSystemOutputHandler(dir, command)
        command.generate(outputHandler)

        command
    }

}
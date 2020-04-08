package io.micronaut.starter.fixture

import io.micronaut.context.BeanContext
import io.micronaut.starter.OutputHandler
import io.micronaut.starter.command.CreateAppCommand
import io.micronaut.starter.command.CreateCliCommand
import io.micronaut.starter.io.FileSystemOutputHandler

trait CommandFixture {

    abstract BeanContext getBeanContext()

    abstract File getDir()

    CreateCliCommand runCreateCliCommand(String lang) {
        CreateCliCommand command = beanContext.getBean(CreateCliCommand)
        command.name = 'example.micronaut.foo'
        command.lang = lang

        OutputHandler outputHandler = new FileSystemOutputHandler(dir, command)
        command.generate(outputHandler)

        command
    }

    CreateAppCommand runCreateAppCommand(String lang, List<String> features = []) {
        CreateAppCommand command = beanContext.getBean(CreateAppCommand)
        command.name = 'example.micronaut.foo'
        command.lang = lang
        command.features = features

        OutputHandler outputHandler = new FileSystemOutputHandler(dir, command)
        command.generate(outputHandler)

        command
    }

}
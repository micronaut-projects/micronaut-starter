package io.micronaut.starter.fixture

import groovy.transform.CompileStatic
import io.micronaut.context.BeanContext
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.OperatingSystem
import io.micronaut.starter.application.generator.ProjectGenerator
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.io.MapOutputHandler
import io.micronaut.starter.options.Options
import io.micronaut.starter.util.NameUtils

@CompileStatic
trait CommandOutputFixture {
    abstract BeanContext getBeanContext()

    Map<String, String> generate(ApplicationType type, Options options, List<String> features = []) {
        def handler = new MapOutputHandler()
        beanContext.getBean(ProjectGenerator).generate(type,
                NameUtils.parse("example.micronaut.foo"),
                options,
                OperatingSystem.LINUX,
                features,
                handler,
                ConsoleOutput.NOOP
        )
        handler.getProject()
    }


    Map<String, String> generate(ApplicationType type , List<String> features = []) {
        def handler = new MapOutputHandler()
        Options options = new Options()
        beanContext.getBean(ProjectGenerator).generate(type,
                NameUtils.parse("example.micronaut.foo"),
                options,
                OperatingSystem.LINUX,
                features,
                handler,
                ConsoleOutput.NOOP
        )
        handler.getProject()
    }
}
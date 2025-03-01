package io.micronaut.starter.fixture

import groovy.transform.CompileStatic
import io.micronaut.context.BeanContext
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.options.OperatingSystem
import io.micronaut.projectgen.core.generator.GeneratorContext
import io.micronaut.projectgen.core.generator.ProjectGenerator
import io.micronaut.projectgen.core.io.ConsoleOutput
import io.micronaut.projectgen.core.io.MapOutputHandler
import io.micronaut.projectgen.core.io.OutputHandler
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.utils.NameUtils

@CompileStatic
trait CommandOutputFixture {
    abstract BeanContext getBeanContext()

    ProjectGenerator getProjectGenerator() {
        beanContext.getBean(ProjectGenerator)
    }

    Map<String, String> generate(Options options) {
        OutputHandler handler = new MapOutputHandler()
        projectGenerator.generate(options,
                handler,
                ConsoleOutput.NOOP
        )
        handler.getProject()
    }

    Map<String, String> generate(List<String> features = []) {
        generate(ApplicationType.DEFAULT, features)
    }

    Map<String, String> generate(String name, List<String> features = []) {
        generate(name, ApplicationType.DEFAULT, features)
    }

    Map<String, String> generate(ApplicationType type, List<String> features = []) {
        generate("example.micronaut.foo", type, features)
    }

    Map<String, String> generate(String name, ApplicationType type, List<String> features = []) {
        OutputHandler handler = new MapOutputHandler()
        Options options = new Options()
        projectGenerator.generate(type,
                NameUtils.parse(name),
                options,
                OperatingSystem.LINUX,
                features,
                handler,
                ConsoleOutput.NOOP
        )
        handler.getProject()
    }

    Map<String, String> generate(ApplicationType type, GeneratorContext generatorContext) {
        OutputHandler handler = new MapOutputHandler()
        projectGenerator.generate(type,
                NameUtils.parse("example.micronaut.foo"),
                handler,
                generatorContext)
        handler.getProject()
    }
}

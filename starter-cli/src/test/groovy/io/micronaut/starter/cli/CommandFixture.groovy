package io.micronaut.starter.cli

import io.micronaut.context.BeanContext
import io.micronaut.core.util.functional.ThrowingSupplier
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.options.OperatingSystem
import io.micronaut.projectgen.core.generator.ProjectGenerator
import io.micronaut.projectgen.core.io.ConsoleOutput
import io.micronaut.projectgen.core.io.FileSystemOutputHandler
import io.micronaut.projectgen.core.io.OutputHandler
import io.micronaut.projectgen.core.buildtools.BuildTool
import io.micronaut.projectgen.core.options.Language
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.utils.NameUtils

trait CommandFixture {

    abstract BeanContext getBeanContext()

    abstract File getDir()

    void generateProject(Language lang,
                         BuildTool buildTool = BuildTool.DEFAULT_OPTION,
                         List<String> features = [],
                         ApplicationType applicationType = ApplicationType.DEFAULT) {
        beanContext.getBean(ProjectGenerator).generate(applicationType,
                NameUtils.parse("example.micronaut.foo"),
                new Options(lang, null, buildTool),
                OperatingSystem.LINUX,
                features,
                new FileSystemOutputHandler(dir, ConsoleOutput.NOOP),
                ConsoleOutput.NOOP
        )
    }

    void generateProject(Options options,
                         List<String> features = [],
                         ApplicationType applicationType = ApplicationType.DEFAULT) {
        beanContext.getBean(ProjectGenerator).generate(applicationType,
                NameUtils.parse("example.micronaut.foo"),
                options,
                OperatingSystem.LINUX,
                features,
                new FileSystemOutputHandler(dir, ConsoleOutput.NOOP),
                ConsoleOutput.NOOP
        )
    }

    void generateGrpcProject(Language lang,
                             BuildTool buildTool = BuildTool.DEFAULT_OPTION,
                             List<String> features = []) {
        beanContext.getBean(ProjectGenerator).generate(ApplicationType.GRPC,
                NameUtils.parse("example.micronaut.foo"),
                new Options(lang, null, buildTool),
                OperatingSystem.LINUX,
                features,
                new FileSystemOutputHandler(dir, ConsoleOutput.NOOP),
                ConsoleOutput.NOOP
        )
    }

    ThrowingSupplier<OutputHandler, IOException> getOutputHandler(ConsoleOutput consoleOutput) {
        return () -> new FileSystemOutputHandler(dir, consoleOutput)
    }
}

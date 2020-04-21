package io.micronaut.starter.fixture

import io.micronaut.context.BeanContext
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.options.Options
import io.micronaut.starter.application.generator.ProjectGenerator
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.io.FileSystemOutputHandler
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.util.NameUtils

trait CommandFixture {

    abstract BeanContext getBeanContext()

    abstract File getDir()

    void generateCliProject(Language lang, BuildTool buildTool = BuildTool.GRADLE, List<String> features = []) {
        beanContext.getBean(ProjectGenerator).generate(ApplicationType.CLI,
                NameUtils.parse("example.micronaut.foo"),
                new Options(lang, null, buildTool),
                features,
                new FileSystemOutputHandler(dir, ConsoleOutput.NOOP),
                ConsoleOutput.NOOP
        )
    }

    void generateDefaultProject(Language lang, BuildTool buildTool = BuildTool.GRADLE, List<String> features = []) {
        beanContext.getBean(ProjectGenerator).generate(ApplicationType.DEFAULT,
                NameUtils.parse("example.micronaut.foo"),
                new Options(lang, null, buildTool),
                features,
                new FileSystemOutputHandler(dir, ConsoleOutput.NOOP),
                ConsoleOutput.NOOP
        )
    }

    void generateGrpcProject(Language lang, BuildTool buildTool = BuildTool.GRADLE, List<String> features = []) {
        beanContext.getBean(ProjectGenerator).generate(ApplicationType.GRPC,
                NameUtils.parse("example.micronaut.foo"),
                new Options(lang, null, buildTool),
                features,
                new FileSystemOutputHandler(dir, ConsoleOutput.NOOP),
                ConsoleOutput.NOOP
        )
    }

    void generateMessagingProject(Language lang, BuildTool buildTool = BuildTool.GRADLE, List<String> features = []) {
        beanContext.getBean(ProjectGenerator).generate(ApplicationType.MESSAGING,
                NameUtils.parse("example.micronaut.foo"),
                new Options(lang, null, buildTool, Collections.emptyMap()),
                features,
                new FileSystemOutputHandler(dir, ConsoleOutput.NOOP),
                ConsoleOutput.NOOP
        )
    }

}
package io.micronaut.starter.fixture

import groovy.transform.CompileStatic
import io.micronaut.context.BeanContext
import io.micronaut.context.Qualifier
import io.micronaut.inject.qualifiers.Qualifiers
import io.micronaut.projectgen.core.feature.AvailableFeatures
import io.micronaut.projectgen.core.generator.Project
import io.micronaut.projectgen.core.options.JdkVersion
import io.micronaut.projectgen.micronaut.ApplicationType
import io.micronaut.projectgen.core.options.OperatingSystem
import io.micronaut.projectgen.core.generator.GeneratorContext
import io.micronaut.projectgen.core.generator.ProjectGenerator
import io.micronaut.projectgen.core.io.ConsoleOutput
import io.micronaut.projectgen.core.io.MapOutputHandler
import io.micronaut.projectgen.core.io.OutputHandler
import io.micronaut.projectgen.core.options.Options
import io.micronaut.projectgen.core.utils.NameUtils
import io.micronaut.projectgen.micronaut.MicronautOptions
import jakarta.inject.Provider

@CompileStatic
trait CommandOutputFixture {
    abstract BeanContext getBeanContext()

    ProjectGenerator getProjectGenerator() {
        beanContext.getBean(ProjectGenerator)
    }

    Map<String, String> generate(Options options) {
        OutputHandler handler = new MapOutputHandler()
        AvailableFeatures availableFeatures = beanContext.getBean(AvailableFeatures, Qualifiers.byName(((MicronautOptions) options).applicationType().name().toLowerCase()))
        projectGenerator.generate(options,
                handler,
                ConsoleOutput.NOOP,
                () -> availableFeatures
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
        Project project = NameUtils.parse(name)
        Options options = MicronautOptions.builder()
                .name(project.name)
                .packageName(project.packageName)
                .applicationType(type)
                .features(features)
                .operatingSystem(OperatingSystem.LINUX)
                .javaVersion(JdkVersion.JDK_17)
                .build()
        AvailableFeatures availableFeatures = beanContext.getBean(AvailableFeatures, Qualifiers.byName(type.name().toLowerCase()))
        projectGenerator.generate(options, handler, () -> availableFeatures)
        handler.getProject()
    }
}

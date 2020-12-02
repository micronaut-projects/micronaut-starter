package io.micronaut.starter.test

import io.micronaut.context.BeanContext
import io.micronaut.core.util.functional.ThrowingSupplier
import io.micronaut.starter.application.ApplicationType
import io.micronaut.starter.application.generator.ProjectGenerator
import io.micronaut.starter.io.ConsoleOutput
import io.micronaut.starter.io.FileSystemOutputHandler
import io.micronaut.starter.io.OutputHandler
import io.micronaut.starter.options.BuildTool
import io.micronaut.starter.options.Language
import io.micronaut.starter.options.Options
import io.micronaut.starter.options.TestFramework
import io.micronaut.starter.util.NameUtils
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.util.environment.OperatingSystem
import java.nio.file.Files
import java.time.Duration

abstract class CommandSpec extends Specification {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()
    @Shared GradleRunner gradleRunner = GradleRunner.create()

    abstract String getTempDirectoryPrefix()

    File dir

    void setup() {
        dir = Files.createTempDirectory(tempDirectoryPrefix).toFile()
    }

    void cleanup() {
        dir.delete()
    }

    String executeBuild(BuildTool buildTool, String command) {
        String output = null
        if (buildTool.isGradle()) {
            output = executeGradle(command).getOutput()
        } else if (buildTool == BuildTool.MAVEN) {
            output = executeMaven(command)
        }
        return output
    }

    BuildResult executeGradle(String command) {
        BuildResult result =
                gradleRunner.withProjectDir(dir)
                        .withArguments(command.split(' '))
                        .build()
        return result
    }

    String executeMaven(String command) {
        if (OperatingSystem.current.isWindows()) {
            command = dir.getAbsolutePath()+"\\"+"mvnw.bat " + command
        } else {
            command = "./mvnw " + command
        }
        String[] args = command.split(" ")
        ProcessBuilder pb = new ProcessBuilder(args)
        Map<String, String> env = pb.environment()
        env["JAVA_HOME"] = System.getenv("JAVA_HOME")

        Process process = pb.directory(dir).start()

        StringBuilder output = new StringBuilder()
        def thread = process.consumeProcessOutputStream(output)
        try {
            thread.join(Duration.ofSeconds(180).toMillis())
        } catch (InterruptedException e) {
        }

        try {
            return output.toString()
        } finally {
            process.destroy()
        }
    }

    void generateProject(Language lang,
                         BuildTool buildTool = BuildTool.GRADLE,
                         List<String> features = [],
                         ApplicationType applicationType = ApplicationType.DEFAULT,
                         TestFramework testFramework = null) {
        beanContext.getBean(ProjectGenerator).generate(applicationType,
                NameUtils.parse("example.micronaut.foo"),
                new Options(lang, testFramework, buildTool),
                io.micronaut.starter.application.OperatingSystem.LINUX,
                features,
                new FileSystemOutputHandler(dir, ConsoleOutput.NOOP),
                ConsoleOutput.NOOP
        )
    }


    ThrowingSupplier<OutputHandler, IOException> getOutputHandler(ConsoleOutput consoleOutput) {
        return { -> new FileSystemOutputHandler(dir, consoleOutput)}
    }

}

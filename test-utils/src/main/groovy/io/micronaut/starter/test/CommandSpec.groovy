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
import org.apache.maven.cli.MavenCli
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions
import spock.util.environment.OperatingSystem

import java.nio.charset.StandardCharsets
import java.nio.file.Files

abstract class CommandSpec extends Specification {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()
    @Shared GradleRunner gradleRunner = GradleRunner.create()
    @Shared MavenCli cli = new MavenCli()

    abstract String getTempDirectoryPrefix()

    File dir
    StringBuilder output
    Process process

    void setupSpec() {
        Thread shutdownHook = new Thread({ killProcess() } as Runnable)
        Runtime.runtime.addShutdownHook(shutdownHook)
    }

    void setup() {
        dir = Files.createTempDirectory(tempDirectoryPrefix).toFile()
        output = new StringBuilder()
    }

    void cleanup() {
        dir.delete()
        killProcess()
    }

    String executeBuild(BuildTool buildTool, String command) {
        String output = null
        if (buildTool == BuildTool.GRADLE) {
            output = executeGradle(command).getOutput()
        } else if (buildTool == BuildTool.MAVEN) {
            output = executeMaven(command)
        }
        return output
    }

    BuildResult executeGradle(String command) {
        BuildResult result =
                gradleRunner.withProjectDir(dir)
                        .withArguments(command)
                        .build()
        return result
    }

    String executeMaven(String command) {
        System.setProperty("maven.multiModuleProjectDirectory", dir.absolutePath)
        def bytesOut = new ByteArrayOutputStream()
        PrintStream output = new PrintStream(bytesOut)
        cli.doMain(command.split(' '), dir.absolutePath, output, output)
        return new String(bytesOut.toByteArray(), StandardCharsets.UTF_8)
    }

    void executeGradleCommand(String command) {
        StringBuilder gradleCommand = new StringBuilder()
        if (OperatingSystem.current.isWindows()) {
            gradleCommand.append("gradlew.bat")
        } else {
            gradleCommand.append("./gradlew")
        }
        gradleCommand.append(" --no-daemon ").append(command)
        executeCommand(gradleCommand)
    }

    PollingConditions getDefaultPollingConditions() {
        new PollingConditions(timeout: 180, initialDelay: 3, delay: 1, factor: 1)
    }

    void testOutputContains(String value) {
        defaultPollingConditions.eventually {
            assert output.toString().contains(value)
        }
    }

    void executeMavenCommand(String command) {
        StringBuilder mavenCommand = new StringBuilder()
        if (OperatingSystem.current.isWindows()) {
            mavenCommand.append("mvnw.bat")
        } else {
            mavenCommand.append("./mvnw")
        }
        mavenCommand.append(" ").append(command)
        executeCommand(mavenCommand)
    }

    private void executeCommand(StringBuilder builder) {
        String[] args = builder.toString().split(" ")
        ProcessBuilder pb = new ProcessBuilder(args)
        Map<String, String> env = pb.environment()
        env["JAVA_HOME"] = System.getenv("JAVA_HOME")
        process = pb.directory(dir).start()
        process.consumeProcessOutputStream(output)
    }

    void killProcess() {
        if (process) {
            process.destroy()
            try {
                process.waitForOrKill(1000)
            } catch(e) {
                process.destroyForcibly()
            }
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

package io.micronaut.starter.generator

import io.micronaut.context.BeanContext
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions
import spock.util.environment.OperatingSystem

import java.nio.file.Files

abstract class CommandSpec extends Specification implements CommandFixture {

    @Shared
    @AutoCleanup
    BeanContext beanContext = BeanContext.run()

    abstract String getTempDirectoryPrefix()

    File dir
    StringBuilder output
    Process process

    void setupSpec() {
        Thread shutdownHook = new Thread(this::killProcess)
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
        new PollingConditions(timeout: 120, initialDelay: 3, delay: 1, factor: 1)
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
}

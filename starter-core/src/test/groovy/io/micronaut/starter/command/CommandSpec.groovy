package io.micronaut.starter.command

import spock.lang.Specification
import spock.util.concurrent.PollingConditions
import spock.util.environment.OperatingSystem

import java.nio.file.Files

class CommandSpec extends Specification {

    File dir = Files.createTempDirectory('mn-starter').toFile()
    StringBuilder output
    Process process

    void setupSpec() {
        Thread shutdownHook = new Thread(this::killProcess)
        Runtime.runtime.addShutdownHook(shutdownHook)
    }

    void setup() {
        output = new StringBuilder()
    }

    void cleanup() {
        dir.delete()
        killProcess()
    }

    void cleanupSpec() {
        String jps = "jps -l".execute().text
        jps.eachLine { String l ->
            String[] parts = l.split(" ")
            if (parts.size() == 2 && parts[1] == "example.micronaut.Application") {
                "kill -9 ${parts[0]}".execute().waitFor()
            }
        }
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

package io.micronaut.starter.command

import spock.lang.Specification
import spock.util.concurrent.PollingConditions
import spock.util.environment.OperatingSystem

import java.nio.file.Files

class CommandSpec extends Specification {

    File dir = Files.createTempDirectory('mn-starter').toFile()
    ByteArrayOutputStream baos = new ByteArrayOutputStream()

    void cleanup() {
        dir.delete()
    }

    Process executeGradleCommand(String command) {
        StringBuilder gradle = new StringBuilder()
        if (OperatingSystem.current.isWindows()) {
            gradle.append("gradlew.bat")
        } else {
            gradle.append("./gradlew")
        }
        gradle.append(" ").append(command)

        Process process = gradle.toString().execute(["JAVA_HOME=${System.getenv("JAVA_HOME")}"], dir)
        process.consumeProcessOutputStream(baos)

        process
    }

    PollingConditions getDefaultPollingConditions() {
        new PollingConditions(timeout: 40, initialDelay: 3, delay: 1, factor: 1)
    }

    void testOutputContains(String value) {
        defaultPollingConditions.eventually {
            assert !baos.toString().contains("Fail")
            assert baos.toString().contains(value)
        }
    }

    Process executeMavenCommand(String command) {
        StringBuilder gradle = new StringBuilder()
        if (OperatingSystem.current.isWindows()) {
            gradle.append("mvnw.bat")
        } else {
            gradle.append("./mvnw")
        }
        gradle.append(" ").append(command)

        Process process = gradle.toString().execute(["JAVA_HOME=${System.getenv("JAVA_HOME")}"], dir)
        process.consumeProcessOutputStream(baos)

        process
    }
}

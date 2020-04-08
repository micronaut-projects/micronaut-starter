package io.micronaut.starter.command

import spock.lang.Specification
import spock.util.concurrent.PollingConditions
import spock.util.environment.OperatingSystem

import java.nio.file.Files

class CommandSpec extends Specification {

    File dir = Files.createTempDirectory('mn-starter').toFile()
    ByteArrayOutputStream baos = new ByteArrayOutputStream()

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

    PollingConditions getPollingConditions() {
        new PollingConditions(timeout: 30, initialDelay: 3, delay: 1, factor: 1)
    }

}

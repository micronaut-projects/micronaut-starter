package io.micronaut.starter.command

import spock.lang.Specification
import spock.util.environment.OperatingSystem

class CommandSpec extends Specification {

    Process executeGradleCommand(String command, File dir) {
        StringBuilder gradle = new StringBuilder()
        if (OperatingSystem.current.isWindows()) {
            gradle.append("gradlew.bat")
        } else {
            gradle.append("./gradlew")
        }
        gradle.append(" ").append(command)

        gradle.toString().execute(["JAVA_HOME=${System.getenv("JAVA_HOME")}"], dir)
    }

    Process executeMavenCommand(String command, File dir) {
        StringBuilder gradle = new StringBuilder()
        if (OperatingSystem.current.isWindows()) {
            gradle.append("mvnw.bat")
        } else {
            gradle.append("./mvnw")
        }
        gradle.append(" ").append(command)

        gradle.toString().execute(["JAVA_HOME=${System.getenv("JAVA_HOME")}"], dir)
    }
}
